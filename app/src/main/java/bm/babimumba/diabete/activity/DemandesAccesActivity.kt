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
import bm.babimumba.diabete.adapter.DemandeAccesAdapter
import bm.babimumba.diabete.databinding.ActivityDemandesAccesBinding
import bm.babimumba.diabete.model.DemandeAcces
import bm.babimumba.diabete.model.Medecin
import bm.babimumba.diabete.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class DemandesAccesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDemandesAccesBinding
    private val demandes = mutableListOf<DemandeAcces>()
    private lateinit var adapter: DemandeAccesAdapter
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemandesAccesBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        setupToolbar()
        setupRecyclerView()
        loadDemandesAcces()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = DemandeAccesAdapter(
            demandes,
            onAccepter = { demande -> accepterDemande(demande) },
            onRefuser = { demande -> refuserDemande(demande) }
        )

        binding.recyclerViewDemandes.apply {
            layoutManager = LinearLayoutManager(this@DemandesAccesActivity)
            adapter = this@DemandesAccesActivity.adapter
        }
    }

    private fun loadDemandesAcces() {
        val patientId = auth.currentUser?.uid
        if (patientId != null) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvAucuneDemande.visibility = View.GONE

            db.collection("partages")
                .whereEqualTo("patientId", patientId)
                .whereEqualTo("statut", "en_attente")
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        binding.progressBar.visibility = View.GONE
                        binding.tvAucuneDemande.visibility = View.VISIBLE
                    } else {
                        loadDemandesWithMedecinInfo(documents.map { it.data })
                    }
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    binding.tvAucuneDemande.visibility = View.VISIBLE
                    binding.tvAucuneDemande.text = "Erreur: ${e.message}"
                    Log.e("DemandesAccesActivity", "Erreur chargement demandes: ${e.message}")
                }
        }
    }

    private fun loadDemandesWithMedecinInfo(partagesData: List<Map<String, Any>>) {
        var loadedCount = 0
        demandes.clear()

        for (partageData in partagesData) {
            val medecinId = partageData["medecinId"] as? String
            if (medecinId != null) {
                db.collection(Constant.USER_COLLECTION)
                    .document(medecinId)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val medecin = document.toObject(Medecin::class.java)
                            medecin?.let { med ->
                                val demande = DemandeAcces(
                                    id = partageData["id"] as? String ?: "",
                                    patientId = partageData["patientId"] as? String ?: "",
                                    medecinId = medecinId,
                                    nomMedecin = "${med.nom} ${med.prenom}",
                                    emailMedecin = med.email,
                                    specialiteMedecin = med.specialite,
                                    hopitalMedecin = med.hopital,
                                    dateHeureDemande = partageData["dateHeurePartage"] as? String ?: "",
                                    statut = partageData["statut"] as? String ?: "en_attente"
                                )
                                demandes.add(demande)
                            }
                        }
                        loadedCount++
                        
                        if (loadedCount == partagesData.size) {
                            binding.progressBar.visibility = View.GONE
                            adapter.notifyDataSetChanged()
                            
                            if (demandes.isEmpty()) {
                                binding.tvAucuneDemande.visibility = View.VISIBLE
                            } else {
                                binding.tvAucuneDemande.visibility = View.GONE
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        loadedCount++
                        Log.e("DemandesAccesActivity", "Erreur chargement médecin: ${e.message}")
                        if (loadedCount == partagesData.size) {
                            binding.progressBar.visibility = View.GONE
                            if (demandes.isEmpty()) {
                                binding.tvAucuneDemande.visibility = View.VISIBLE
                            }
                        }
                    }
            } else {
                loadedCount++
            }
        }
    }

    private fun accepterDemande(demande: DemandeAcces) {
        db.collection("partages")
            .document(demande.id)
            .update("statut", "accepte")
            .addOnSuccessListener {
                demandes.remove(demande)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Accès accordé au Dr. ${demande.nomMedecin}", Toast.LENGTH_SHORT).show()
                
                if (demandes.isEmpty()) {
                    binding.tvAucuneDemande.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun refuserDemande(demande: DemandeAcces) {
        db.collection("partages")
            .document(demande.id)
            .update("statut", "refuse")
            .addOnSuccessListener {
                demandes.remove(demande)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Demande refusée", Toast.LENGTH_SHORT).show()
                
                if (demandes.isEmpty()) {
                    binding.tvAucuneDemande.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
} 