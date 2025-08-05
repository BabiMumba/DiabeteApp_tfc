package bm.babimumba.diabete.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.adapter.DonneeMedicaleAdapter
import bm.babimumba.diabete.databinding.ActivityIntegrityCheckBinding
import bm.babimumba.diabete.model.DonneeMedicale
import bm.babimumba.diabete.repository.UserRepository
import bm.babimumba.diabete.utils.IntegrityManager
import com.google.firebase.auth.FirebaseAuth

class IntegrityCheckActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityIntegrityCheckBinding
    private val integrityManager = IntegrityManager()
    private val userRepository = UserRepository()
    private val donnees = mutableListOf<DonneeMedicale>()
    private lateinit var adapter: DonneeMedicaleAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntegrityCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Retour à l'activité précédente
        }
        setupRecyclerView()
        setupButtons()
        loadMedicalData()
    }
    
    private fun setupRecyclerView() {
        adapter = DonneeMedicaleAdapter(donnees) { donnee ->
            // Action lors du clic sur une donnée
            showIntegrityDetails(donnee)
        }
        
        binding.recyclerViewIntegrity.apply {
            layoutManager = LinearLayoutManager(this@IntegrityCheckActivity)
            adapter = this@IntegrityCheckActivity.adapter
        }
    }
    
    private fun setupButtons() {
        binding.btnCheckAllIntegrity.setOnClickListener {
            checkAllDataIntegrity()
        }
        
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun loadMedicalData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            binding.progressBar.visibility = View.VISIBLE
            
            userRepository.getDonneesMedicalesPatient(
                patientId = userId,
                onSuccess = { donneesList ->
                    binding.progressBar.visibility = View.GONE
                    donnees.clear()
                    donnees.addAll(donneesList)
                    adapter.notifyDataSetChanged()
                    
                    if (donnees.isEmpty()) {
                        binding.tvNoData.visibility = View.VISIBLE
                    } else {
                        binding.tvNoData.visibility = View.GONE
                    }
                },
                onError = { error ->
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Erreur: $error", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
    
    private fun checkAllDataIntegrity() {
        if (donnees.isEmpty()) {
            Toast.makeText(this, "Aucune donnée à vérifier", Toast.LENGTH_SHORT).show()
            return
        }
        
        binding.progressBar.visibility = View.VISIBLE
        binding.btnCheckAllIntegrity.isEnabled = false
        
        integrityManager.verifyMultipleMedicalDataIntegrity(donnees) { results ->
            binding.progressBar.visibility = View.GONE
            binding.btnCheckAllIntegrity.isEnabled = true
            
            val verifiedCount = results.values.count { it }
            val totalCount = results.size
            
            // Mettre à jour l'interface avec les résultats
            updateIntegrityResults(results)
            
            Toast.makeText(
                this,
                "Vérification terminée: $verifiedCount/$totalCount données vérifiées",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    private fun updateIntegrityResults(results: Map<String, Boolean>) {
        // Mettre à jour les données avec les résultats de vérification
        donnees.forEach { donnee ->
            val isVerified = results[donnee.id] ?: false
            // Ici vous pourriez mettre à jour le statut d'intégrité de la donnée
        }
        
        adapter.notifyDataSetChanged()
    }
    
    private fun showIntegrityDetails(donnee: DonneeMedicale) {
        // Afficher les détails d'intégrité d'une donnée spécifique
        val message = buildString {
            appendLine("ID: ${donnee.id}")
            appendLine("Date: ${donnee.dateHeure}")
            appendLine("Glycémie: ${donnee.glycemie}")
            appendLine("Hash d'intégrité: ${donnee.integrityHash ?: "Non généré"}")
            appendLine("Statut: ${donnee.dataIntegrityStatus}")
            appendLine("Vérifié blockchain: ${if (donnee.blockchainVerified) "Oui" else "Non"}")
        }
        
        android.app.AlertDialog.Builder(this)
            .setTitle("Détails d'intégrité")
            .setMessage(message)
            .setPositiveButton("Vérifier maintenant") { _, _ ->
                checkSingleDataIntegrity(donnee)
            }
            .setNegativeButton("Fermer", null)
            .show()
    }
    
    private fun checkSingleDataIntegrity(donnee: DonneeMedicale) {
        binding.progressBar.visibility = View.VISIBLE
        
        integrityManager.verifyMedicalDataIntegrity(donnee) { isVerified, hash ->
            binding.progressBar.visibility = View.GONE
            
            val message = if (isVerified) {
                "✅ Donnée vérifiée avec succès!\nHash: $hash"
            } else {
                "❌ Échec de la vérification d'intégrité"
            }
            
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
} 