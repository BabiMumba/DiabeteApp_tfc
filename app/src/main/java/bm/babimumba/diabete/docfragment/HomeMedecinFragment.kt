package bm.babimumba.diabete.docfragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.activity.CreerPrescriptionActivity
import bm.babimumba.diabete.activity.DetailActivity
import bm.babimumba.diabete.activity.ScannerQrActivity
import bm.babimumba.diabete.adapter.PatientMedecinAdapter
import bm.babimumba.diabete.databinding.FragmentHomeMedecinBinding
import bm.babimumba.diabete.model.Patient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeMedecinFragment : Fragment() {
    private lateinit var binding: FragmentHomeMedecinBinding
    private lateinit var patientAdapter: PatientMedecinAdapter
    private val patients = mutableListOf<Patient>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMedecinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        loadPatients()
    }

    private fun setupRecyclerView() {
        patientAdapter = PatientMedecinAdapter(
            patients,
            onVoirDonneesClick = { patient ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("patient_id", patient.id)
                startActivity(intent)
            },
            onPrescrireClick = { patient ->
                val intent = Intent(requireContext(), CreerPrescriptionActivity::class.java)
                intent.putExtra("patient_id", patient.id)
                startActivity(intent)
            }
        )

        binding.recyclerViewPatients.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = patientAdapter
        }
    }

    private fun setupFab() {
        binding.fabScanner.setOnClickListener {
            val intent = Intent(requireContext(), ScannerQrActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadPatients() {
        val medecinId = auth.currentUser?.uid
        if (medecinId != null) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvAucunPatient.visibility = View.GONE

            db.collection("partages")
                .whereEqualTo("medecinId", medecinId)
                .whereEqualTo("statut", "accepte")
                .get()
                .addOnSuccessListener { documents ->
                    val patientIds = documents.mapNotNull { it.getString("patientId") }
                    
                    if (patientIds.isNotEmpty()) {
                        loadPatientDetails(patientIds)
                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.tvAucunPatient.visibility = View.VISIBLE
                    }
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    binding.tvAucunPatient.visibility = View.VISIBLE
                    binding.tvAucunPatient.text = "Erreur: ${e.message}"
                }
        }
    }

    private fun loadPatientDetails(patientIds: List<String>) {
        var loadedCount = 0
        patients.clear()

        for (patientId in patientIds) {
            db.collection("patients").document(patientId).get()
                .addOnSuccessListener { document ->
                    val patient = document.toObject(Patient::class.java)
                    patient?.let {
                        it.id = document.id
                        patients.add(it)
                    }
                    loadedCount++
                    
                    if (loadedCount == patientIds.size) {
                        binding.progressBar.visibility = View.GONE
                        patientAdapter.notifyDataSetChanged()
                        
                        if (patients.isEmpty()) {
                            binding.tvAucunPatient.visibility = View.VISIBLE
                        } else {
                            binding.tvAucunPatient.visibility = View.GONE
                        }
                    }
                }
                .addOnFailureListener { e ->
                    loadedCount++
                    if (loadedCount == patientIds.size) {
                        binding.progressBar.visibility = View.GONE
                        binding.tvAucunPatient.visibility = View.VISIBLE
                        binding.tvAucunPatient.text = "Erreur: ${e.message}"
                    }
                }
        }
    }
} 