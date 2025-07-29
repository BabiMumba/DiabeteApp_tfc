package bm.babimumba.diabete.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddMesureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMesureBinding
    private val addMesureViewModel: AddMesureViewModel by viewModels()
    private var selectedDate: String = ""
    private var selectedTimestamp: Long? = null

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
            val activiteSelectionnee = binding.btnSelectActivity.selectedItem?.toString() ?: ""
            val duree = binding.min.text.toString().trim()
            val commentaire = binding.comment.text.toString().trim()
            
            // Logs de débogage
            android.util.Log.d("AddMesureActivity", "Activité sélectionnée: $activiteSelectionnee")
            android.util.Log.d("AddMesureActivity", "Durée: $duree")
            android.util.Log.d("AddMesureActivity", "Commentaire: $commentaire")
            
            // Validation simple
            if (glycemie.isEmpty() || (binding.tvSelectedDateTime.text.contains("non sélectionnées"))) {
                Toast.makeText(this, "Veuillez saisir la glycémie et la date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            // Utiliser le timestamp sélectionné ou l'instant courant
            val timestamp = selectedTimestamp ?: System.currentTimeMillis()
            
            // Construire l'activité avec la durée si disponible
            val activite = if (activiteSelectionnee.isNotEmpty() && activiteSelectionnee != "Activité Physique") {
                if (duree.isNotEmpty()) {
                    "$activiteSelectionnee ($duree min)"
                } else {
                    activiteSelectionnee
                }
            } else null
            
            val donnee = DonneeMedicale(
                patientId = userId,
                dateHeure = timestamp.toString(),
                glycemie = glycemie,
                insuline = if (insuline.isEmpty()) null else insuline,
                activite = activite,
                commentaire = if (commentaire.isEmpty()) null else commentaire
            )
            
            android.util.Log.d("AddMesureActivity", "DonneeMedicale créée: activite=${donnee.activite}, commentaire=${donnee.commentaire}")
            addMesureViewModel.ajouterDonneeMedicale(donnee)
        }
    }

    //fonction pour selection la date et l'heure
    fun selectdateHours(){
        binding.btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal.set(year, month, dayOfMonth)
                    
                    // Après la sélection de la date, ouvrir le TimePickerDialog
                    val timePickerDialog = TimePickerDialog(
                        this,
                        { _, hourOfDay, minute ->
                            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            cal.set(Calendar.MINUTE, minute)
                            selectedTimestamp = cal.timeInMillis
                            
                            // Formater la date et heure pour l'affichage
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            val formattedDateTime = dateFormat.format(cal.time)
                            binding.tvSelectedDateTime.text = formattedDateTime
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    )
                    timePickerDialog.show()
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }
}
