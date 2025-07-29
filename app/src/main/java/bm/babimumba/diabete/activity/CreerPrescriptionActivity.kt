package bm.babimumba.diabete.activity

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.R
import bm.babimumba.diabete.adapter.CreerPrescriptionMedicamentAdapter
import bm.babimumba.diabete.databinding.ActivityCreerPrescriptionBinding
import bm.babimumba.diabete.model.Medicament
import bm.babimumba.diabete.model.Patient
import bm.babimumba.diabete.model.Prescription
import bm.babimumba.diabete.utils.Constant
import bm.babimumba.diabete.utils.DateUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CreerPrescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreerPrescriptionBinding
    private lateinit var medicamentAdapter: CreerPrescriptionMedicamentAdapter
    private val medicaments = mutableListOf<Medicament>()
    private var patient: Patient? = null
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreerPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        setupToolbar()
        setupRecyclerView()
        setupButtons()
        loadPatientInfo()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupRecyclerView() {
        medicamentAdapter = CreerPrescriptionMedicamentAdapter(medicaments) { position ->
            medicaments.removeAt(position)
            medicamentAdapter.notifyItemRemoved(position)
            updateMedicamentCount()
        }
        
        binding.recyclerViewMedicaments.apply {
            layoutManager = LinearLayoutManager(this@CreerPrescriptionActivity)
            adapter = medicamentAdapter
        }
    }

    private fun setupButtons() {
        binding.btnAjouterMedicament.setOnClickListener {
            showAddMedicamentDialog()
        }

        binding.btnSauvegarder.setOnClickListener {
            sauvegarderPrescription()
        }
    }

    private fun loadPatientInfo() {
        val patientId = intent.getStringExtra("patient_id")
        if (patientId != null) {
            db.collection(Constant.USER_COLLECTION).document(patientId).get()
                .addOnSuccessListener { document ->
                    patient = document.toObject(Patient::class.java)
                    val age = DateUtils.calculateAge(patient?.date_naissance ?: "")
                    patient?.let {
                        binding.tvNomPatient.text = "${it.name} ${it.postnom}"
                        binding.tvAgePatient.text = "$age ans"
                    }
                }
        }
    }

    private fun showAddMedicamentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_ajouter_medicament, null)
        
        AlertDialog.Builder(this)
            .setTitle("Ajouter un médicament")
            .setView(dialogView)
            .setPositiveButton("Ajouter") { _, _ ->
                val nom = dialogView.findViewById<android.widget.EditText>(R.id.etNomMedicament).text.toString()
                val dosage = dialogView.findViewById<android.widget.EditText>(R.id.etDosage).text.toString()
                val frequence = dialogView.findViewById<android.widget.EditText>(R.id.etFrequence).text.toString()
                val duree = dialogView.findViewById<android.widget.EditText>(R.id.etDuree).text.toString()
                val instructions = dialogView.findViewById<android.widget.EditText>(R.id.etInstructions).text.toString()
                val quantite = dialogView.findViewById<android.widget.EditText>(R.id.etQuantite).text.toString()

                if (nom.isNotEmpty() && dosage.isNotEmpty()) {
                    val medicament = Medicament(nom, dosage, frequence, duree, instructions, quantite)
                    medicaments.add(medicament)
                    medicamentAdapter.notifyItemInserted(medicaments.size - 1)
                    updateMedicamentCount()
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun updateMedicamentCount() {
        binding.tvNombreMedicaments.text = "${medicaments.size} médicament(s)"
    }

    private fun sauvegarderPrescription() {
        val instructions = binding.etInstructions.text.toString()
        val dureeTraitement = binding.etDureeTraitement.text.toString()
        val notes = binding.etNotes.text.toString()

        if (medicaments.isEmpty()) {
            binding.etInstructions.error = "Ajoutez au moins un médicament"
            return
        }

        if (instructions.isEmpty()) {
            binding.etInstructions.error = "Instructions requises"
            return
        }

        val prescription = Prescription(
            id = UUID.randomUUID().toString(),
            patientId = patient?.id ?: "",
            medecinId = auth.currentUser?.uid ?: "",
            datePrescription = Date(),
            medicaments = medicaments.toList(),
            instructions = instructions,
            dureeTraitement = dureeTraitement,
            notes = notes
        )

        binding.progressBar.visibility = View.VISIBLE
        binding.btnSauvegarder.isEnabled = false

        db.collection("prescriptions").document(prescription.id).set(prescription)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                AlertDialog.Builder(this)
                    .setTitle("Succès")
                    .setMessage("Prescription créée avec succès")
                    .setPositiveButton("OK") { _, _ ->
                        finish()
                    }
                    .show()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                binding.btnSauvegarder.isEnabled = true
                AlertDialog.Builder(this)
                    .setTitle("Erreur")
                    .setMessage("Erreur lors de la création: ${e.message}")
                    .setPositiveButton("OK", null)
                    .show()
            }
    }
} 