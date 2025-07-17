package bm.babimumba.diabete.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.ActivityAddMesureBinding
import bm.babimumba.diabete.viewmodel.AddMesureViewModel
import bm.babimumba.diabete.viewmodel.AddMesureState
import bm.babimumba.diabete.model.DonneeMedicale
import com.google.firebase.auth.FirebaseAuth

class AddMesureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMesureBinding
    private val addMesureViewModel: AddMesureViewModel by viewModels()
    private var selectedDate: String = ""

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

        // Observer l'état d'ajout
        addMesureViewModel.addMesureState.observe(this, Observer { state ->
            when (state) {
                is AddMesureState.Loading -> {
                    binding.circularLoader.loaderFrameLayout.visibility = View.VISIBLE
                }
                is AddMesureState.Success -> {
                    binding.circularLoader.loaderFrameLayout.visibility = View.GONE
                    Toast.makeText(this, "Mesure ajoutée avec succès", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is AddMesureState.Error -> {
                    binding.circularLoader.loaderFrameLayout.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        // Ajout de la logique pour enregistrer la mesure
        binding.btnSave?.setOnClickListener {
            val glycemie = binding.etNiveauGlycemie.text.toString().trim()
            val insuline = binding.insulineRapide.text.toString().trim()
            val dateHeure = selectedDate.ifEmpty { binding.btnSelectDate.text.toString() }
            val activity = binding.btnSelectActivity.selectedItem.toString()
            val commentaire = binding.comment.text.toString().trim()
            // Validation simple
            if (glycemie.isEmpty()) {
                Toast.makeText(this, "Veuillez saisir la glycémie ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            // Convertir la date en timestamp pour un meilleur tri
            val timestamp = System.currentTimeMillis()
            val donnee = DonneeMedicale(
                patientId = userId,
                dateHeure = timestamp.toString(),
                glycemie = glycemie,
                activite = activity,
                commentaire = commentaire,
                insuline = if (insuline.isEmpty()) null else insuline
            )
            addMesureViewModel.ajouterDonneeMedicale(donnee)
        }
    }

    //fonction pour selection la date et l'heure
    fun selectdateHours(){
        binding.btnSelectDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // Mettre à jour le TextView avec la date sélectionnée
                    val dateStr = "$dayOfMonth/${month + 1}/$year"
                    binding.btnSelectDate.text = dateStr
                    selectedDate = dateStr
                },
                2025, 7, 7//aout = 7
            )
            datePickerDialog.show()
        }
    }
}
