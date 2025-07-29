package bm.babimumba.diabete.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.R
import bm.babimumba.diabete.adapter.MedicamentAdapter
import bm.babimumba.diabete.databinding.ActivityDetailPrescriptionBinding
import bm.babimumba.diabete.model.Medicament
import bm.babimumba.diabete.model.Medecin
import bm.babimumba.diabete.model.Patient
import bm.babimumba.diabete.model.Prescription
import bm.babimumba.diabete.utils.Constant
import bm.babimumba.diabete.utils.DateUtils
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DetailPrescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPrescriptionBinding
    private lateinit var medicamentAdapter: MedicamentAdapter
    private val medicaments = mutableListOf<Medicament>()
    private val db = FirebaseFirestore.getInstance()
    private var prescription: Prescription? = null
    private var medecin: Medecin? = null
    private var patient: Patient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPrescriptionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        setupToolbar()
        setupRecyclerView()
        loadPrescriptionDetails()
        setupButtons()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        medicamentAdapter = MedicamentAdapter(medicaments)
        binding.recyclerViewMedicaments.apply {
            layoutManager = LinearLayoutManager(this@DetailPrescriptionActivity)
            adapter = medicamentAdapter
        }
    }

    private fun setupButtons() {
        binding.btnModifier.setOnClickListener {
            // TODO: Implémenter la modification de prescription
            Toast.makeText(this, "Fonctionnalité à venir", Toast.LENGTH_SHORT).show()
        }

        binding.btnExporter.setOnClickListener {
            // TODO: Implémenter l'export PDF
            Toast.makeText(this, "Export PDF à venir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPrescriptionDetails() {
        val prescriptionId = intent.getStringExtra("prescription_id")
        if (prescriptionId != null) {
            binding.progressBar.visibility = View.VISIBLE
            
            db.collection("prescriptions")
                .document(prescriptionId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        prescription = document.toObject(Prescription::class.java)
                        prescription?.let { pres ->
                            pres.id = document.id
                            displayPrescriptionInfo(pres)
                            loadMedecinInfo(pres.medecinId)
                            loadPatientInfo(pres.patientId)
                        }
                    } else {
                        Toast.makeText(this, "Prescription non trouvée", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    binding.progressBar.visibility = View.GONE
                }
                .addOnFailureListener { e ->
                    Log.e("DetailPrescriptionActivity", "Erreur lors du chargement: ${e.message}")
                    Toast.makeText(this, "Erreur lors du chargement", Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                    finish()
                }
        } else {
            Toast.makeText(this, "ID de prescription manquant", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun displayPrescriptionInfo(prescription: Prescription) {
        // Date de prescription
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.tvDatePrescription.text = "Date : ${sdf.format(prescription.datePrescription)}"
        
        // Statut
        val statutText = when (prescription.statut) {
            "active" -> "Active"
            "termine" -> "Terminée"
            "annule" -> "Annulée"
            else -> prescription.statut
        }
        binding.tvStatut.text = "Statut : $statutText"
        
        // Instructions
        binding.tvInstructions.text = prescription.instructions.ifEmpty { "Aucune instruction spécifique" }
        
        // Notes
        binding.tvNotes.text = prescription.notes.ifEmpty { "Aucune note" }
        
        // Médicaments
        medicaments.clear()
        medicaments.addAll(prescription.medicaments)
        medicamentAdapter.notifyDataSetChanged()
    }

    private fun loadMedecinInfo(medecinId: String) {
        db.collection(Constant.USER_COLLECTION)
            .document(medecinId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    medecin = document.toObject(Medecin::class.java)
                    medecin?.let { med ->
                        binding.tvNomMedecin.text = "Dr. ${med.nom} ${med.prenom}"
                        binding.tvSpecialiteMedecin.text = med.specialite
                        binding.tvHopitalMedecin.text = med.hopital
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("DetailPrescriptionActivity", "Erreur chargement médecin: ${e.message}")
            }
    }

    private fun loadPatientInfo(patientId: String) {
        db.collection(Constant.USER_COLLECTION)
            .document(patientId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    patient = document.toObject(Patient::class.java)
                    patient?.let { pat ->
                        binding.tvNomPatient.text = "${pat.name} ${pat.postnom}"
                        
                        // Calculer l'âge à partir de la date de naissance
                        val age = DateUtils.calculateAge(pat.date_naissance)
                        binding.tvAgePatient.text = "$age ans"
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("DetailPrescriptionActivity", "Erreur chargement patient: ${e.message}")
            }
    }
} 