package bm.babimumba.diabete.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.adapter.PrescriptionAdapter
import bm.babimumba.diabete.databinding.ActivityMesPrescriptionsBinding
import bm.babimumba.diabete.model.Prescription
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MesPrescriptionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMesPrescriptionsBinding
    private lateinit var prescriptionAdapter: PrescriptionAdapter
    private val prescriptions = mutableListOf<Prescription>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMesPrescriptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadPrescriptions()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupRecyclerView() {
        prescriptionAdapter = PrescriptionAdapter(prescriptions) { prescription ->
            // Ouvrir le détail de la prescription
            val intent = Intent(this, DetailPrescriptionActivity::class.java)
            intent.putExtra("prescription_id", prescription.id)
            startActivity(intent)
        }
        
        binding.recyclerViewPrescriptions.apply {
            layoutManager = LinearLayoutManager(this@MesPrescriptionsActivity)
            adapter = prescriptionAdapter
        }
    }

    private fun loadPrescriptions() {
        val patientId = auth.currentUser?.uid
        if (patientId != null) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvAucunePrescription.visibility = View.GONE

            db.collection("prescriptions")
                .whereEqualTo("patientId", patientId)
                .orderBy("datePrescription", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    binding.progressBar.visibility = View.GONE
                    prescriptions.clear()
                    
                    for (document in documents) {
                        val prescription = document.toObject(Prescription::class.java)
                        prescription.let {
                            it.id = document.id
                            prescriptions.add(it)
                        }
                    }
                    
                    prescriptionAdapter.notifyDataSetChanged()
                    
                    if (prescriptions.isEmpty()) {
                        binding.tvAucunePrescription.visibility = View.VISIBLE
                    } else {
                        binding.tvAucunePrescription.visibility = View.GONE
                    }
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    binding.tvAucunePrescription.visibility = View.VISIBLE
                    binding.tvAucunePrescription.text = "Erreur: ${e.message}"
                    Log.e("MesPrescriptionsActivity", "Error loading", e)
                }
        }
    }
} 