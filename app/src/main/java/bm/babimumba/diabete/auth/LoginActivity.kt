package bm.babimumba.diabete.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import bm.babimumba.diabete.MainActivity
import bm.babimumba.diabete.R
import bm.babimumba.diabete.activity.MainActivityMedecin
import bm.babimumba.diabete.databinding.ActivityLoginBinding
import bm.babimumba.diabete.utils.RoleManager
import kotlinx.coroutines.MainScope

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSave.btnModelUi.text = "Se connecter"
        //change text color
        binding.btnSave.btnModelUi.setTextColor(resources.getColor(R.color.white))
        //change tint color
        binding.btnSave.btnModelUi.backgroundTintList = resources.getColorStateList(R.color.primary)

        binding.btnSave.btnModelUi.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.progressBar.visibility = android.view.View.VISIBLE
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Récupérer le profil utilisateur dans Firestore
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            val db = FirebaseFirestore.getInstance()
                            db.collection("patients")
                                .document(userId)
                                .get()
                                .addOnSuccessListener { document ->
                                    binding.progressBar.visibility = android.view.View.GONE
                                    if (document.exists()) {
                                        val role = document.getString("role")
                                        if (role != null) {
                                            // Sauvegarder le rôle
                                            RoleManager.saveUserRole(this, role, userId)
                                            
                                            // Rediriger selon le rôle
                                            when (role) {
                                                "patient" -> {
                                                    val intent = Intent(this, MainActivity::class.java)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                "medecin" -> {
                                                    val intent = Intent(this, MainActivityMedecin::class.java)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                else -> {
                                                    Toast.makeText(this, "Rôle non reconnu.", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        } else {
                                            Toast.makeText(this, "Rôle non défini.", Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        Toast.makeText(this, "Profil utilisateur introuvable.", Toast.LENGTH_LONG).show()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    binding.progressBar.visibility = android.view.View.GONE
                                    Toast.makeText(this, "Erreur lors de la récupération du profil.", Toast.LENGTH_LONG).show()
                                }
                        } else {
                            binding.progressBar.visibility = android.view.View.GONE
                            Toast.makeText(this, "Erreur d'authentification.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        binding.progressBar.visibility = android.view.View.GONE
                        Toast.makeText(this, "Email ou mot de passe incorrect.", Toast.LENGTH_LONG).show()
                    }
                }
        }
        binding.textRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}