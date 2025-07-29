package bm.babimumba.diabete.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import bm.babimumba.diabete.databinding.ActivityScannerQrBinding
import bm.babimumba.diabete.model.Partage
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.util.Date

class ScannerQrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScannerQrBinding
    private val db = FirebaseFirestore.getInstance()

    private val scanLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        if (result.contents != null) {
            // QR code scanné avec succès
            val patientId = result.contents
            checkPatientAccess(patientId)
        } else {
            Toast.makeText(this, "Scan annulé", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerQrBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        // Forcer le mode portrait
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setupToolbar()
        setupScanner()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupScanner() {
        binding.btnScanner.setOnClickListener {
            startQrScanner()
        }
    }

    private fun startQrScanner() {
        val options = ScanOptions()
            .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            .setPrompt("Scannez le QR code du patient")
            .setCameraId(0)
            .setBeepEnabled(true)
            .setBarcodeImageEnabled(true)
            .setOrientationLocked(true)
            .setTorchEnabled(false)

        scanLauncher.launch(options)
    }

    private fun checkPatientAccess(patientId: String) {
        // Vérifier si le médecin a accès à ce patient
        val medecinId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        
        if (medecinId != null) {
            db.collection("partages")
                .whereEqualTo("patientId", patientId)
                .whereEqualTo("medecinId", medecinId)
                .whereEqualTo("statut", "accepte")
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // Accès autorisé, ouvrir la fiche patient
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("patient_id", patientId)
                        startActivity(intent)
                        finish()
                    } else {
                        // Pas d'accès, demander l'autorisation
                        requestPatientAccess(patientId)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun requestPatientAccess(patientId: String) {
        val medecinId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        
        if (medecinId != null) {
            // Créer une demande de partage
            val partage = Partage(
                id = java.util.UUID.randomUUID().toString(),
                patientId = patientId,
                medecinId = medecinId,
                dateHeurePartage = Date().toString(),
                statut = "en_attente"
            )

            db.collection("partages").document(partage.id).set(partage)
                .addOnSuccessListener {
                    Toast.makeText(this, "Demande d'accès envoyée au patient", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
} 