package bm.babimumba.diabete.repository

import bm.babimumba.diabete.model.Patient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import bm.babimumba.diabete.model.DonneeMedicale
import android.util.Log

class UserRepository {
    fun registerPatient(
        name: String,
        postnom: String,
        email: String,
        poids: String,
        dateNaissance: String,
        sexe: String,
        password: String,
        role: String,
        onSuccess: (Patient) -> Unit,
        onError: (String) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val patient = Patient(
                        id = user?.uid ?: "",
                        name = name,
                        postnom = postnom,
                        email = email,
                        poids = poids,
                        date_naissance = dateNaissance,
                        sexe = sexe,
                        date_inscription = System.currentTimeMillis().toString(),
                        type_diabete = "TYPE_2",
                        role = role
                    )
                    val db = FirebaseFirestore.getInstance()
                    db.collection("patients")
                        .document(user?.uid ?: "")
                        .set(patient)
                        .addOnSuccessListener {
                            onSuccess(patient)
                        }
                        .addOnFailureListener { e ->
                            onError(e.localizedMessage ?: "Erreur lors de l'enregistrement Firestore")
                        }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        onError("L'email existe déjà.")
                    } else {
                        onError(exception?.localizedMessage ?: "Erreur d'inscription")
                    }
                }
            }
    }

    fun ajouterDonneeMedicale(
        donnee: DonneeMedicale,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("donnees_medicales").document()
        val donneeAvecId = donnee.copy(id = docRef.id)
        docRef.set(donneeAvecId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.localizedMessage ?: "Erreur lors de l'ajout de la donnée médicale") }
    }

    fun getDonneesMedicalesPatient(
        patientId: String,
        onSuccess: (List<bm.babimumba.diabete.model.DonneeMedicale>) -> Unit,
        onError: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection("donnees_medicales")
            .whereEqualTo("patientId", patientId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { it.toObject(bm.babimumba.diabete.model.DonneeMedicale::class.java) }
                onSuccess(list)
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: "Erreur lors de la récupération des données médicales")
            }
    }

    fun getDerniereDonneeMedicale(
        patientId: String,
        onSuccess: (bm.babimumba.diabete.model.DonneeMedicale?) -> Unit,
        onError: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection("donnees_medicales")
            .whereEqualTo("patientId", patientId)
            .orderBy("dateHeure", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val derniereMesure = if (result.documents.isNotEmpty()) {
                    result.documents.first().toObject(DonneeMedicale::class.java)
                } else null
                onSuccess(derniereMesure)
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: "Erreur lors de la récupération de la dernière mesure")
            }
    }

    fun getDonneesTendances(
        patientId: String,
        onSuccess: (List<bm.babimumba.diabete.model.DonneeMedicale>) -> Unit,
        onError: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection("donnees_medicales")
            .whereEqualTo("patientId", patientId)
            .orderBy("dateHeure", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val mesures = result.documents.mapNotNull {
                    it.toObject(bm.babimumba.diabete.model.DonneeMedicale::class.java)
                }
                Log.d("UserRepository", "Mesures récupérées: ${mesures.size}")
                // Filtrer les 7 derniers jours côté client
                val dateLimite = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 10) // 7 jours en arrière
                val mesuresFiltrees = mesures.filter { mesure ->
                    // Convertir la date String en timestamp pour comparaison
                    try {
                        val dateMesure = mesure.dateHeure.toLongOrNull() ?: 0L
                        dateMesure >= dateLimite
                    } catch (e: Exception) {
                        Log.d("UserRepository", "Erreur conversion date: ${mesure.dateHeure}")
                        true // Si conversion échoue, inclure la mesure
                    }
                }
                Log.d("UserRepository", "Mesures filtrées: ${mesuresFiltrees.size}")
                onSuccess(mesuresFiltrees)
            }
            .addOnFailureListener { e ->
                Log.e("UserRepository", "Erreur Firestore: ${e.message}")
                onError(e.localizedMessage ?: "Erreur lors de la récupération des tendances")
            }
    }

    fun calculerStatistiques(mesures: List<bm.babimumba.diabete.model.DonneeMedicale>): Triple<Double, Double, Double> {
        if (mesures.isEmpty()) return Triple(0.0, 0.0, 0.0)

        val valeurs = mesures.mapNotNull { it.glycemie.toDoubleOrNull() }
        if (valeurs.isEmpty()) return Triple(0.0, 0.0, 0.0)

        val min = valeurs.minOrNull() ?: 0.0
        val max = valeurs.maxOrNull() ?: 0.0
        val moyenne = valeurs.average()

        return Triple(min, moyenne, max)
    }
} 