package bm.babimumba.diabete.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.ActivityDetailBinding
import bm.babimumba.diabete.model.DonneeMedicale
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    private val db = FirebaseFirestore.getInstance()
    private var donneeId: String? = null
    private var donnee: DonneeMedicale? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Récupérer l'ID de la donnée depuis l'intent
        donneeId = intent.getStringExtra("donnee_id")
        
        if (donneeId != null) {
            // Charger les données depuis Firestore
            loadDonneeDetails()
        } else {
            Toast.makeText(this, "Erreur: ID de donnée manquant", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Initialize toolbar buttons
        setupToolbarButtons()
        
        // Initialize delete button
        setupDeleteButton()
    }
    
    private fun setupToolbarButtons() {
        
        // Edit button
        binding.btnEdit.setOnClickListener {
            Toast.makeText(this, "Modifier la mesure", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun loadDonneeDetails() {
        if (donneeId == null) return
        
        db.collection("donnees_medicales")
            .document(donneeId!!)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    donnee = document.toObject(DonneeMedicale::class.java)
                    donnee?.let { 
                        it.id = document.id
                        displayDonneeDetails(it)
                    }
                } else {
                    Toast.makeText(this, "Donnée non trouvée", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun displayDonneeDetails(donnee: DonneeMedicale) {
        // Afficher la date et l'heure
        val date = formatTimestamp(donnee.dateHeure)
        binding.tvDate.text = date
        
        // Afficher la glycémie
        if (!donnee.glycemie.isNullOrEmpty()) {
            binding.tvGlycemie.text = donnee.glycemie
        }
        
        // Afficher le repas
        binding.tvRepas.text = donnee.repas ?: "Pas de détails"
        
        // Afficher l'insuline
        binding.tvInsuline.text = donnee.insuline ?: "-"
        
        // Afficher l'activité
        binding.tvActivite.text = donnee.activite ?: "-"
        
        // Afficher le commentaire
        binding.tvCommentaire.text = donnee.commentaire ?: "-"
        
        // Afficher la source
        val sourceText = when (donnee.source) {
            "medecin" -> "Médecin"
            "patient" -> "Patient"
            else -> donnee.source
        }
        binding.tvSource.text = sourceText
        
        // Afficher la sensation (basée sur la glycémie)
        val sensation = getSensationFromGlycemie(donnee.glycemie)
        binding.tvSensation.text = sensation
    }

    private fun formatTimestamp(timestampStr: String): String {
        return try {
            val timestamp = timestampStr.toLongOrNull() ?: return timestampStr
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val timeSdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = Date(timestamp)
            "${sdf.format(date)} à ${timeSdf.format(date)}"
        } catch (e: Exception) {
            timestampStr
        }
    }

    private fun getSensationFromGlycemie(glycemie: String?): String {
        if (glycemie.isNullOrEmpty()) return "Normale"
        
        return try {
            val valeur = glycemie.toDouble()
            when {
                valeur < 70 -> "Hypoglycémie"
                valeur <= 140 -> "Normale"
                else -> "Hyperglycémie"
            }
        } catch (e: Exception) {
            "Normale"
        }
    }

    private fun setupDeleteButton() {
        binding.delete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }
    
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmer la suppression")
            .setMessage("Êtes-vous sûr de vouloir supprimer cette mesure ?")
            .setPositiveButton("Supprimer") { _, _ ->
                deleteDonnee()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun deleteDonnee() {
        if (donneeId == null) return
        
        db.collection("donnees_medicales")
            .document(donneeId!!)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Mesure supprimée", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}