package bm.babimumba.diabete.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import bm.babimumba.diabete.MainActivity
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.ActivityRegisterBinding
import bm.babimumba.diabete.model.Patient
import bm.babimumba.diabete.utils.Constant
import bm.babimumba.diabete.utils.Constant.PREF_KEY_USER_NAME
import bm.babimumba.diabete.utils.Constant.PREF_NAME
import bm.babimumba.diabete.utils.VOID
import bm.babimumba.diabete.viewmodel.RegisterViewModel
import bm.babimumba.diabete.viewmodel.RegisterState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSave.btnModelUi.text = "Cree un compte"
        //change text color
        binding.btnSave.btnModelUi.setTextColor(resources.getColor(R.color.white))
        //change tint color
        binding.btnSave.btnModelUi.backgroundTintList = resources.getColorStateList(R.color.primary)

        // Observer l'état d'inscription
        registerViewModel.registerState.observe(this, Observer { state ->
            when (state) {
                is RegisterState.Loading -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                }
                is RegisterState.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    VOID.showSnackBar(binding.root, "Inscription réussie")
                    VOID.Intent1(this, MainActivity::class.java)
                    finish()
                }
                is RegisterState.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    VOID.showSnackBar(binding.root, state.message)
                }
            }
        })

        binding.btnSave.btnModelUi.setOnClickListener {
            if (ChampValide()) {
                // Récupérer le rôle sélectionné
                val selectedRole = if (binding.radioGroupRole.checkedRadioButtonId == R.id.radioMedecin) "medecin" else "patient"
                // Appel ViewModel
                registerViewModel.registerPatient(
                    name = binding.name.text.toString(),
                    postnom = binding.postnom.text.toString(),
                    email = binding.email.text.toString(),
                    poids = binding.poindEdt.text.toString(),
                    dateNaissance = binding.btnDateNaissance.text.toString(),
                    sexe = binding.spinnerSexe.selectedItem.toString(),
                    password = binding.password.text.toString(),
                    role = selectedRole
                )
            }
        }
        binding.textRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        // Initialiser le sélecteur de date
        pickerdate()
    }


    //fonction pour selectionner la date de naissance
    fun pickerdate(){
        // Utiliser un DatePickerDialog pour sélectionner la date de naissance
        binding.btnDateNaissance.setOnClickListener {
            val datePickerDialog = android.app.DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // Mettre à jour le TextView avec la date sélectionnée
                    binding.btnDateNaissance.text = "$dayOfMonth/${month + 1}/$year"
                },
                2000, 0, 1 // Date par défaut (1er janvier 2000)
            )
            datePickerDialog.show()
        }
    }

    //fonction pour verifie si tous le champ sont rempli
    private fun  ChampValide():Boolean {
        return if (binding.name.text.length < 3 || binding.name.text.isEmpty() || binding.name.text.length > 15) {
            VOID.ShowToast(this, "Verifiez votre nom caractere min 3 max 15")
            false
        } else if (binding.postnom.text.length < 3 || binding.postnom.text.isEmpty() || binding.postnom.text.length > 15) {
            VOID.ShowToast(this, "Verifiez votre postnom caractere min 3 max 15")
            false
        }else if (binding.poindEdt.text.isEmpty()) {
            VOID.ShowToast(this, "Completez votre poids")
            false
        } else if (binding.btnDateNaissance.text.contains("Naissance")){
            VOID.ShowToast(this, "Selectionnez votre date de naissance")
            false
        }else if (binding.spinnerSexe.selectedItem.toString()== "Sexe"){
            VOID.ShowToast(this, "Selectionnez votre genre")
            false
        }else if (binding.password.text.isEmpty()) {
            VOID.ShowToast(this, "Entrer le mot de passe")
            false
        }else if (binding.confirmPassword.text.isEmpty()) {
            VOID.ShowToast(this, "Entrer le mot de passe")
            false
        }else if (binding.password.text.toString() != binding.confirmPassword.text.toString()) {
            VOID.ShowToast(this, "le mot de passe ne correspond pas")
            false
        }
        else {
            true
        }
    }


    //auth with email & password
    private  fun registerwithEmail(){
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        if (email.isEmpty() || password.isEmpty()){
            VOID.showSnackBar(binding.root, "Veuillez remplir tous les champs")
            return
        }
        VOID.loading(true,binding.progressBar,binding.btnSave.btnModelUi)
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    StoreDataFistore()

                } else {
                    VOID.loading(false,binding.progressBar,binding.btnSave.btnModelUi)
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        // L'email existe déjà
                        showEmailExistsDialog(email)
                    } else {
                        // Autres erreurs
                        VOID.showSnackBar(binding.root, "Erreur : ${exception?.localizedMessage}")
                    }
                    Log.w("RegisterActivity", "signInWithEmail:failure", task.exception)
                }
            }
    }
    private fun saveUserDataInfo(pr:String) {
        val sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(PREF_KEY_USER_NAME, pr)
        editor.apply()
        VOID.showSnackBar(binding.root, "Inscription réussie")
        VOID.loading(false,binding.progressBar,binding.btnSave.btnModelUi)
        VOID.Intent1(this, MainActivity::class.java)
    }
    private fun StoreDataFistore(){
        val AUTH = FirebaseAuth.getInstance()
        val FIREBASE_USER = AUTH.currentUser
        val mail = FIREBASE_USER?.email
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val date_dins = sdf.format(Date())
        val database = FirebaseFirestore.getInstance()
        val DATA_USER:MutableMap<String,Any> = HashMap()
        DATA_USER["name"] = binding.name.text.toString()
        DATA_USER["postnom"] = binding.postnom.text.toString()
        DATA_USER["email"] = mail.toString()
        DATA_USER["poids"] = binding.poindEdt.text.toString()
        DATA_USER["date_naissance"] = binding.btnDateNaissance.text.toString()
        DATA_USER["sexe"] = binding.spinnerSexe.selectedItem.toString()
        DATA_USER["date_inscription"] = date_dins
        DATA_USER["type_diabete"] = "TYPE_2"
        // Récupérer le rôle sélectionné dans le RadioGroup
        val selectedRole = if (binding.radioGroupRole.checkedRadioButtonId == R.id.radioMedecin) "medecin" else "patient"
        DATA_USER["role"] = selectedRole
        DATA_USER["id"] = FIREBASE_USER?.uid.toString()
        database.collection(Constant.PATIENT_COLLECTION)
            .document(FIREBASE_USER?.uid.toString())
            .set(DATA_USER)
            .addOnSuccessListener {
                val fullname = "${binding.name.text} ${binding.postnom.text}"
                saveUserDataInfo(fullname)
            }
            .addOnFailureListener { e ->
                Log.w("RegisterActivity", "Error writing document", e)
            }

    }

    // Fonction pour afficher le dialogue
    private fun showEmailExistsDialog(email: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Adresse existante")
        builder.setMessage("L'adresse $email existe déjà. Voulez-vous vous connecter ?")
        builder.setPositiveButton("Se connecter") { _, _ ->
            // Action pour rediriger vers la connexion
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("email", email) // Pré-remplir l'email si nécessaire
            startActivity(intent)
        }
        builder.setNegativeButton("Annuler", null)
        builder.show()
    }


}