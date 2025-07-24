package bm.babimumba.diabete.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.ActivityPartageQrBinding

class PartageQrActivity : AppCompatActivity() {
    lateinit var binding: ActivityPartageQrBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPartageQrBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Retour à l'activité précédente
        }


    }
}