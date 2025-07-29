package bm.babimumba.diabete.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.activity.DetailPrescriptionActivity
//import bm.babimumba.diabete.activity.DetailPrescriptionActivity
import bm.babimumba.diabete.adapter.PrescriptionAdapter
import bm.babimumba.diabete.databinding.FragmentHistoriqueMedecinBinding
import bm.babimumba.diabete.model.Prescription
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HistoriqueMedecinFragment : Fragment() {
    private lateinit var binding: FragmentHistoriqueMedecinBinding
    private lateinit var prescriptionAdapter: PrescriptionAdapter
    private val prescriptions = mutableListOf<Prescription>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoriqueMedecinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadPrescriptions()
    }

    private fun setupRecyclerView() {
                prescriptionAdapter = PrescriptionAdapter(prescriptions) { prescription ->
            // Ouvrir le détail de la prescription
            val intent = Intent(requireContext(), DetailPrescriptionActivity::class.java)
            intent.putExtra("prescription_id", prescription.id)
            startActivity(intent)
        }
        
        binding.recyclerViewPrescriptions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = prescriptionAdapter
        }
    }

    private fun loadPrescriptions() {
        val medecinId = auth.currentUser?.uid
        if (medecinId != null) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvAucunePrescription.visibility = View.GONE

            db.collection("prescriptions")
                .whereEqualTo("medecinId", medecinId)
                .orderBy("datePrescription", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    binding.progressBar.visibility = View.GONE
                    prescriptions.clear()
                    
                    for (document in documents) {
                        val prescription = document.toObject(Prescription::class.java)
                        prescription?.let {
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
                    Log.d("HistoriqueMedecinFragment", "Erreur lors du chargement des prescriptions: ${e.message}")
                }
        }
    }
} 