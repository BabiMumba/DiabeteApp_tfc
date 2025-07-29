package bm.babimumba.diabete.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import bm.babimumba.diabete.R
import bm.babimumba.diabete.activity.MainActivityMedecin
import bm.babimumba.diabete.databinding.ActivityRegisterMedecinBinding
import bm.babimumba.diabete.model.Medecin
import bm.babimumba.diabete.utils.Constant
import bm.babimumba.diabete.utils.Constant.PREF_KEY_USER_NAME
import bm.babimumba.diabete.utils.Constant.PREF_NAME
import bm.babimumba.diabete.utils.RoleManager
import bm.babimumba.diabete.utils.VOID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

class RegisterMedecinActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterMedecinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterMedecinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        setupUI()
        setupSpecialiteAutocomplete()
        setupListeners()
    }

    private fun setupUI() {
        binding.btnSave.btnModelUi.text = "Créer un compte médecin"
        binding.btnSave.btnModelUi.setTextColor(resources.getColor(R.color.white))
        binding.btnSave.btnModelUi.backgroundTintList = resources.getColorStateList(R.color.primary)
        
        // Hôpital par défaut
        binding.hopital.setText("Hôpital Public de Référence Tertiaire Jason Sendwe")
    }

    private fun setupSpecialiteAutocomplete() {
        val specialites = resources.getStringArray(R.array.specialites_medecin)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, specialites)
        binding.specialite.setAdapter(adapter)
        binding.specialite.threshold = 1 // Commencer l'autocomplétion après 1 caractère
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSave.btnModelUi.setOnClickListener {
            if (champValide()) {
                registerMedecin()
            }
        }

        binding.textRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun champValide(): Boolean {
        return when {
            binding.name.text!!.length < 3 || binding.name.text!!.isEmpty() || binding.name.text!!.length > 15 -> {
                VOID.ShowToast(this, "Vérifiez votre nom (3-15 caractères)")
                false
            }
            binding.postnom.text!!.length < 3 || binding.postnom.text!!.isEmpty() || binding.postnom.text!!.length > 15 -> {
                VOID.ShowToast(this, "Vérifiez votre prénom (3-15 caractères)")
                false
            }
            binding.email.text!!.isEmpty() -> {
                VOID.ShowToast(this, "Entrez votre email")
                false
            }
            binding.specialite.text!!.isEmpty() -> {
                VOID.ShowToast(this, "Entrez votre spécialité")
                false
            }
            binding.numeroOrdre.text!!.isEmpty() -> {
                VOID.ShowToast(this, "Entrez votre numéro d'ordre")
                false
            }
            binding.hopital.text.toString().isEmpty() -> {
                VOID.ShowToast(this, "L'hôpital est requis")
                false
            }
            binding.adresse.text.toString().isEmpty() -> {
                VOID.ShowToast(this, "Entrez votre adresse")
                false
            }
            binding.telephone.text.toString().isEmpty() -> {
                VOID.ShowToast(this, "Entrez votre téléphone")
                false
            }
            binding.password.text.toString().isEmpty() -> {
                VOID.ShowToast(this, "Entrez le mot de passe")
                false
            }
            binding.confirmPassword.text!!.isEmpty() -> {
                VOID.ShowToast(this, "Confirmez le mot de passe")
                false
            }
            binding.password.text.toString() != binding.confirmPassword.text.toString() -> {
                VOID.ShowToast(this, "Les mots de passe ne correspondent pas")
                false
            }
            else -> true
        }
    }

    private fun registerMedecin() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        
        if (email.isEmpty() || password.isEmpty()) {
            VOID.showSnackBar(binding.root, "Veuillez remplir tous les champs")
            return
        }
        
        VOID.loading(true, binding.progressBar, binding.btnSave.btnModelUi)
        val auth = FirebaseAuth.getInstance()
        
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storeMedecinData()
                } else {
                    VOID.loading(false, binding.progressBar, binding.btnSave.btnModelUi)
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        showEmailExistsDialog(email)
                    } else {
                        VOID.showSnackBar(binding.root, "Erreur : ${exception?.localizedMessage}")
                    }
                    Log.w("RegisterMedecinActivity", "signInWithEmail:failure", task.exception)
                }
            }
    }

    private fun storeMedecinData() {
        val auth = FirebaseAuth.getInstance()
        val firebaseUser = auth.currentUser
        val email = firebaseUser?.email
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val dateInscription = sdf.format(Date())
        val database = FirebaseFirestore.getInstance()
        
        val medecinData: MutableMap<String, Any> = HashMap()
        medecinData["nom"] = binding.name.text.toString()
        medecinData["prenom"] = binding.postnom.text.toString()
        medecinData["email"] = email.toString()
        medecinData["specialite"] = binding.specialite.text.toString()
        medecinData["numeroOrdre"] = binding.numeroOrdre.text.toString()
        medecinData["hopital"] = binding.hopital.text.toString()
        medecinData["adresse"] = binding.adresse.text.toString()
        medecinData["telephone"] = binding.telephone.text.toString()
        medecinData["dateInscription"] = dateInscription
        medecinData["statut"] = "actif"
        medecinData["role"] = "medecin"
        medecinData["id"] = firebaseUser?.uid.toString()

        database.collection(Constant.USER_COLLECTION)
            .document(firebaseUser?.uid.toString())
            .set(medecinData)
            .addOnSuccessListener {
                val fullName = "${binding.name.text} ${binding.postnom.text}"
                saveUserDataInfo(fullName)
            }
            .addOnFailureListener { e ->
                Log.w("RegisterMedecinActivity", "Error writing document", e)
                VOID.loading(false, binding.progressBar, binding.btnSave.btnModelUi)
                VOID.showSnackBar(binding.root, "Erreur lors de la sauvegarde : ${e.message}")
            }
    }

    private fun saveUserDataInfo(fullName: String) {
        val sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(PREF_KEY_USER_NAME, fullName)
        editor.apply()
        
        VOID.showSnackBar(binding.root, "Inscription réussie")
        VOID.loading(false, binding.progressBar, binding.btnSave.btnModelUi)
        
        // Sauvegarder le rôle médecin et rediriger
        RoleManager.saveUserRole(this, "medecin", FirebaseAuth.getInstance().currentUser?.uid ?: "")
        VOID.Intent1(this, MainActivityMedecin::class.java)
        finish()
    }

    private fun showEmailExistsDialog(email: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Adresse existante")
        builder.setMessage("L'adresse $email existe déjà. Voulez-vous vous connecter ?")
        builder.setPositiveButton("Se connecter") { _, _ ->
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }
        builder.setNegativeButton("Annuler", null)
        builder.show()
    }
}