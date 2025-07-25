package bm.babimumba.diabete.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.adapter.ExportFileAdapter
import bm.babimumba.diabete.databinding.ActivityListExportBinding
import java.io.File

class ListExportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListExportBinding
    private val files = mutableListOf<File>()
    private lateinit var adapter: ExportFileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListExportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ExportFileAdapter(files, onOpen = { ouvrirPdf(it) }, onDelete = { supprimerPdf(it) })
        binding.recyclerViewExports.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewExports.adapter = adapter

        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        chargerExports()
    }

    private fun chargerExports() {
        val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: filesDir
        val pdfs = dir.listFiles { f -> f.extension == "pdf" }?.sortedByDescending { it.lastModified() } ?: emptyList()
        files.clear()
        files.addAll(pdfs)
        adapter.notifyDataSetChanged()
    }

    private fun ouvrirPdf(file: File) {
        val intent = Intent(this, PdfViewerActivity::class.java)
        intent.putExtra("pdf_path", file.absolutePath)
        startActivity(intent)
    }

    private fun supprimerPdf(file: File) {
        if (file.delete()) {
            chargerExports()
        }
    }
}