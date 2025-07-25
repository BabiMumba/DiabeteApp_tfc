package bm.babimumba.diabete.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.ActivityPartageQrBinding
import android.graphics.Bitmap
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.adapter.MedecinAccesAdapter
import bm.babimumba.diabete.model.MedecinAcces
import bm.babimumba.diabete.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class PartageQrActivity : AppCompatActivity() {
    lateinit var binding: ActivityPartageQrBinding
    private val medecins = mutableListOf<MedecinAcces>()
    private lateinit var adapter: MedecinAccesAdapter
    private val userRepository = UserRepository()
    private val userId: String by lazy { FirebaseAuth.getInstance().currentUser?.uid ?: "" }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPartageQrBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Génération et affichage du QR code (ID patient)
        binding.tvCodePartage.text = userId
        binding.qrImageView.setImageBitmap(generateQrCode(userId))
        binding.btnCopyCode.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Code de partage", userId)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Code copié !", Toast.LENGTH_SHORT).show()
        }

        // Liste des médecins ayant accès
        adapter = MedecinAccesAdapter(medecins) { medecin ->
            revoquerAccesMedecin(medecin)
        }
        binding.recyclerViewMedecins.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMedecins.adapter = adapter
        chargerMedecinsAcces()

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Retour à l'activité précédente
        }
    }

    private fun generateQrCode(content: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
        val bmp = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
        for (x in 0 until 512) {
            for (y in 0 until 512) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bmp
    }

    private fun chargerMedecinsAcces() {
        userRepository.getMedecinsAcces(userId,
            onSuccess = {
                medecins.clear()
                medecins.addAll(it)
                adapter.notifyDataSetChanged()
            },
            onError = { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        )
    }

    private fun revoquerAccesMedecin(medecin: MedecinAcces) {
        userRepository.revoquerAccesMedecin(userId, medecin.id,
            onSuccess = {
                medecins.remove(medecin)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Accès révoqué", Toast.LENGTH_SHORT).show()
            },
            onError = { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        )
    }
}