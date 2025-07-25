package bm.babimumba.diabete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bm.babimumba.diabete.databinding.ItemExportFileBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class ExportFileAdapter(
    private val files: List<File>,
    private val onOpen: (File) -> Unit,
    private val onDelete: (File) -> Unit
) : RecyclerView.Adapter<ExportFileAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemExportFileBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExportFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]
        holder.binding.tvFileName.text = file.name
        holder.binding.tvFileDate.text = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date(file.lastModified()))
        holder.itemView.setOnClickListener { onOpen(file) }
        holder.binding.btnDeleteExport.setOnClickListener { onDelete(file) }
    }

    override fun getItemCount(): Int = files.size
} 