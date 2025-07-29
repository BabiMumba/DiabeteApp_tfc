package bm.babimumba.diabete.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bm.babimumba.diabete.databinding.ActivityAddMesureMedecinBinding
import bm.babimumba.diabete.model.DonneeMedicale
import bm.babimumba.diabete.model.Patient
import bm.babimumba.diabete.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddMesureMedecinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMesureMedecinBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val patients = mutableListOf<Patient>()
    private var selectedPatientId: String? = null
    private var selectedDateTime: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMesureMedecinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDateTimePicker()
        loadPatients()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupDateTimePicker() {
        // Afficher la date/heure actuelle
        updateDateTimeDisplay()

        binding.btnDateTime.setOnClickListener {
            showDateTimePicker()
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDateTime

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                        selectedDateTime = calendar.timeInMillis
                        updateDateTimeDisplay()
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateDateTimeDisplay() {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        binding.tvDateTime.text = sdf.format(Date(selectedDateTime))
    }

    private fun loadPatients() {
        val medecinId = auth.currentUser?.uid
        if (medecinId != null) {
            binding.progressBar.visibility = View.VISIBLE

            // Charger les patients qui ont accordé l'accès au médecin
            db.collection("partages")
                .whereEqualTo("medecinId", medecinId)
                .whereEqualTo("statut", "accepte")
                .get()
                .addOnSuccessListener { documents ->
                    val patientIds = documents.map { it.getString("patientId") ?: "" }.filter { it.isNotEmpty() }
                    
                    if (patientIds.isNotEmpty()) {
                        // Charger les détails des patients
                        loadPatientDetails(patientIds)
                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.tvAucunPatient.visibility = View.VISIBLE
                        binding.spinnerPatient.visibility = View.GONE
                    }
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun loadPatientDetails(patientIds: List<String>) {
        val patientNames = mutableListOf<String>()
        patients.clear()

        var loadedCount = 0
        val totalCount = patientIds.size

        patientIds.forEach { patientId ->
            db.collection(Constant.USER_COLLECTION)
                .document(patientId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val patient = document.toObject(Patient::class.java)
                        patient?.let {
                            it.id = document.id
                            patients.add(it)
                            patientNames.add("${it.name} ${it.postnom}")
                        }
                    }
                    loadedCount++
                    
                    if (loadedCount == totalCount) {
                        setupPatientSpinner(patientNames)
                        binding.progressBar.visibility = View.GONE
                    }
                }
                .addOnFailureListener { e ->
                    loadedCount++
                    if (loadedCount == totalCount) {
                        setupPatientSpinner(patientNames)
                        binding.progressBar.visibility = View.GONE
                    }
                }
        }
    }

    private fun setupPatientSpinner(patientNames: List<String>) {
        if (patientNames.isNotEmpty()) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, patientNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPatient.adapter = adapter

            binding.spinnerPatient.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                    selectedPatientId = patients[position].id
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                    selectedPatientId = null
                }
            }

            binding.tvAucunPatient.visibility = View.GONE
            binding.spinnerPatient.visibility = View.VISIBLE
        } else {
            binding.tvAucunPatient.visibility = View.VISIBLE
            binding.spinnerPatient.visibility = View.GONE
        }
    }

    private fun setupSaveButton() {
        binding.btnSauvegarder.setOnClickListener {
            if (validateInputs()) {
                saveMesure()
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (selectedPatientId == null) {
            Toast.makeText(this, "Veuillez sélectionner un patient", Toast.LENGTH_SHORT).show()
            return false
        }

        val glycemie = binding.etGlycemie.text.toString().trim()
        val insuline = binding.etInsuline.text.toString().trim()
        val repas = binding.etRepas.text.toString().trim()
        val activite = binding.etActivite.text.toString().trim()
        val commentaire = binding.etCommentaire.text.toString().trim()

        if (glycemie.isEmpty() && insuline.isEmpty() && repas.isEmpty() && activite.isEmpty() && commentaire.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir au moins un champ", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveMesure() {
        binding.btnSauvegarder.isEnabled = false
        binding.progressBarSave.visibility = View.VISIBLE

        val glycemie = binding.etGlycemie.text.toString().trim()
        val insuline = binding.etInsuline.text.toString().trim()
        val repas = binding.etRepas.text.toString().trim()
        val activite = binding.etActivite.text.toString().trim()
        val commentaire = binding.etCommentaire.text.toString().trim()

        val donneeMedicale = DonneeMedicale(
            patientId = selectedPatientId!!,
            glycemie = glycemie.ifEmpty { null }.toString(),
            insuline = insuline.ifEmpty { null },
            repas = repas.ifEmpty { null },
            activite = activite.ifEmpty { null },
            commentaire = commentaire.ifEmpty { null },
            dateHeure = selectedDateTime.toString(),
            source = "medecin"
        )

        Log.d("AddMesureMedecin", "Sauvegarde mesure: patientId=$selectedPatientId, dateHeure=$selectedDateTime, source=medecin")

        db.collection("donnees_medicales")
            .add(donneeMedicale)
            .addOnSuccessListener { documentReference ->
                Log.d("AddMesureMedecin", "Mesure sauvegardée avec ID: ${documentReference.id}")
                binding.btnSauvegarder.isEnabled = true
                binding.progressBarSave.visibility = View.GONE
                Toast.makeText(this, "Mesure ajoutée avec succès", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("AddMesureMedecin", "Erreur sauvegarde: ${e.message}")
                binding.btnSauvegarder.isEnabled = true
                binding.progressBarSave.visibility = View.GONE
                Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}