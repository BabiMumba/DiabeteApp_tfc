package bm.babimumba.diabete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bm.babimumba.diabete.databinding.ItemMesureBinding
import bm.babimumba.diabete.model.DonneeMedicale

class DonneeMedicaleAdapter(
    private var items: List<DonneeMedicale>
) : RecyclerView.Adapter<DonneeMedicaleAdapter.MesureViewHolder>() {

    inner class MesureViewHolder(val binding: ItemMesureBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesureViewHolder {
        val binding = ItemMesureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MesureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MesureViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            // Affichage de la glycémie
            // Ex: "85 mg/dL"
            this.root.findViewById<android.widget.TextView>(bm.babimumba.diabete.R.id.carte)
            tvGlycemie.text = "${item.glycemie} mg/dL"
            // Affichage de l'heure (extraction de l'heure si possible)
            // Affichage insuline
            tvInsuline.text = if (!item.insuline.isNullOrEmpty()) "Insuline: ${item.insuline} UI" else ""
            // Affichage commentaire ou activité
            tvDetails.text = item.commentaire ?: item.activite ?: "Pas de détails"
            dateheure.text = formatTimestamp(item.dateHeure)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<DonneeMedicale>) {
        items = newItems
        notifyDataSetChanged()
    }
    private fun formatTimestamp(timestampStr: String?): String {
        return try {
            val timestamp = timestampStr?.toLongOrNull() ?: return ""
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
            val date = java.util.Date(timestamp)
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }
} 