package bm.babimumba.diabete.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.adapter.DonneeMedicaleAdapter
import bm.babimumba.diabete.databinding.ActivityPatientDetailBinding
import bm.babimumba.diabete.model.DonneeMedicale
import bm.babimumba.diabete.model.Patient
import bm.babimumba.diabete.utils.Constant
import bm.babimumba.diabete.utils.DateUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PatientDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientDetailBinding
    private val donneesMedicales = mutableListOf<DonneeMedicale>()
    private lateinit var adapter: DonneeMedicaleAdapter
    private val db = FirebaseFirestore.getInstance()
    private var patient: Patient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadPatientData()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            //cela signifie que nous consommons les insets pour
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = DonneeMedicaleAdapter(donneesMedicales)
        binding.recyclerViewDonnees.apply {
            layoutManager = LinearLayoutManager(this@PatientDetailActivity)
            adapter = this@PatientDetailActivity.adapter
        }
    }

    private fun loadPatientData() {
        val patientId = intent.getStringExtra("patient_id")
        if (patientId != null) {
            binding.progressBar.visibility = View.VISIBLE

            // Charger les informations du patient
            db.collection(Constant.USER_COLLECTION)
                .document(patientId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        patient = document.toObject(Patient::class.java)
                        patient?.let { pat ->
                            displayPatientInfo(pat)
                            loadDonneesMedicales(patientId)
                        }
                    } else {
                        Toast.makeText(this, "Patient non trouvé", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
                    finish()
                }
        } else {
            Toast.makeText(this, "ID patient manquant", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun displayPatientInfo(patient: Patient) {
        binding.tvNomPatient.text = "${patient.name} ${patient.postnom}"
        binding.tvEmailPatient.text = patient.email
        binding.tvSexePatient.text = patient.sexe
        binding.tvPoidsPatient.text = "${patient.poids} kg"
        binding.tvAgePatient.text = "${DateUtils.calculateAge(patient.date_naissance)} ans"
        binding.tvTypeDiabete.text = patient.type_diabete
        binding.tvDateInscription.text = patient.date_inscription
    }

    private fun loadDonneesMedicales(patientId: String) {
        db.collection("donnees_medicales")
            .whereEqualTo("patientId", patientId)
            .orderBy("dateHeure", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                binding.progressBar.visibility = View.GONE
                donneesMedicales.clear()
                
                for (document in documents) {
                    val donnee = document.toObject(DonneeMedicale::class.java)
                    donnee?.let {
                        it.id = document.id
                        donneesMedicales.add(it)
                    }
                }
                
                adapter.notifyDataSetChanged()
                
                if (donneesMedicales.isEmpty()) {
                    binding.tvAucuneDonnee.visibility = View.VISIBLE
                } else {
                    binding.tvAucuneDonnee.visibility = View.GONE
                }
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                binding.tvAucuneDonnee.visibility = View.VISIBLE
                binding.tvAucuneDonnee.text = "Erreur: ${e.message}"
                Log.e("PatientDetailActivity", "Erreur chargement données: ${e.message}")
            }
    }
} 