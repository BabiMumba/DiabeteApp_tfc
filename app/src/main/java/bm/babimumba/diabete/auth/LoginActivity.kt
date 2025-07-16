package bm.babimumba.diabete.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import bm.babimumba.diabete.MainActivity
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.ActivityLoginBinding
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.textRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}