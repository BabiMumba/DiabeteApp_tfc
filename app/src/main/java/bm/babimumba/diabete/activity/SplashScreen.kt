package bm.babimumba.diabete.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import bm.babimumba.diabete.MainActivity
import bm.babimumba.diabete.R
import bm.babimumba.diabete.auth.LoginActivity
import bm.babimumba.diabete.databinding.ActivitySplashScreenBinding
import bm.babimumba.diabete.utils.RoleManager
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Handler(Looper.getMainLooper()).postDelayed({
            checkuser()
        }, 2000)
    }

    fun checkuser(){
        //verifier si l'utilisateur est connecté
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in, check role
            val userRole = RoleManager.getUserRole(this)
            if (userRole != null) {
                when (userRole) {
                    "medecin" -> {
                        val intent = Intent(this, MainActivityMedecin::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "patient" -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else -> {
                        // Role non reconnu, rediriger vers login
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                // Pas de rôle sauvegardé, rediriger vers login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            // User is not signed in, redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}