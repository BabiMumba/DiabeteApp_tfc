package bm.babimumba.diabete.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.ActivityAddMesureBinding

class AddMesureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMesureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddMesureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

       /* ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, insets.top, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }*/

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Retour à l'activité précédente
        }
        selectdateHours()

    }

    //fonction pour selection la date et l'heure
    fun selectdateHours(){
        binding.btnSelectDate.setOnClickListener {
            val datePickerDialog = android.app.DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // Mettre à jour le TextView avec la date sélectionnée
                    binding.btnSelectDate.text = "$dayOfMonth/${month + 1}/$year"
                },
                2000, 0, 1 // Date par défaut (1er janvier 2000)
            )
            datePickerDialog.show()
        }
    }
}
