package bm.babimumba.diabete.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bm.babimumba.diabete.databinding.ActivityPdfViewerBinding
import com.github.barteksc.pdfviewer.PDFView
import java.io.File
import android.net.Uri
import androidx.activity.enableEdgeToEdge
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PdfViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            //cela signifie que nous consommons les insets pour
            WindowInsetsCompat.CONSUMED
        }
        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val filePath = intent.getStringExtra("pdf_path")
        if (filePath != null) {
            val file = File(filePath)
            val uri: Uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
            //le nom sera le 15 premiers caractères du nom du fichier
            binding.titreTl.text = file.name.take(15) + "..."
            binding.pdfView.fromUri(uri)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .load()
        }
    }
}