package bm.babimumba.diabete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bm.babimumba.diabete.databinding.ItemDonneeMedicaleBinding
import bm.babimumba.diabete.model.DonneeMedicale
import java.text.SimpleDateFormat
import java.util.*

class DonneeMedicaleAdapter(
    private var donnees: List<DonneeMedicale>
) : RecyclerView.Adapter<DonneeMedicaleAdapter.DonneeViewHolder>() {

    inner class DonneeViewHolder(private val binding: ItemDonneeMedicaleBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(donnee: DonneeMedicale) {
            // Formater la date
            val date = try {
                val timestamp = donnee.dateHeure.toLongOrNull()
                if (timestamp != null) {
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    sdf.format(Date(timestamp))
                } else {
                    donnee.dateHeure
                }
            } catch (e: Exception) {
                donnee.dateHeure
            }
            
            binding.tvDate.text = date
            
            // Glycémie
            if (!donnee.glycemie.isNullOrEmpty()) {
                binding.tvGlycemie.text = "Glycémie : ${donnee.glycemie} mg/dL"
                binding.tvGlycemie.visibility = android.view.View.VISIBLE
            } else {
                binding.tvGlycemie.visibility = android.view.View.GONE
            }
            
            // Insuline
            if (!donnee.insuline.isNullOrEmpty()) {
                binding.tvInsuline.text = "Insuline : ${donnee.insuline} unités"
                binding.tvInsuline.visibility = android.view.View.VISIBLE
            } else {
                binding.tvInsuline.visibility = android.view.View.GONE
            }
            
            // Repas
            if (!donnee.repas.isNullOrEmpty()) {
                binding.tvRepas.text = "Repas : ${donnee.repas}"
                binding.tvRepas.visibility = android.view.View.VISIBLE
            } else {
                binding.tvRepas.visibility = android.view.View.GONE
            }
            
            // Activité
            if (!donnee.activite.isNullOrEmpty()) {
                binding.tvActivite.text = "Activité : ${donnee.activite}"
                binding.tvActivite.visibility = android.view.View.VISIBLE
            } else {
                binding.tvActivite.visibility = android.view.View.GONE
            }
            
            // Commentaire
            if (!donnee.commentaire.isNullOrEmpty()) {
                binding.tvCommentaire.text = "Commentaire : ${donnee.commentaire}"
                binding.tvCommentaire.visibility = android.view.View.VISIBLE
            } else {
                binding.tvCommentaire.visibility = android.view.View.GONE
            }
            
            // Source
            binding.tvSource.text = "Source : ${donnee.source}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonneeViewHolder {
        val binding = ItemDonneeMedicaleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DonneeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonneeViewHolder, position: Int) {
        holder.bind(donnees[position])
    }

    override fun getItemCount(): Int = donnees.size

    fun updateData(newDonnees: List<DonneeMedicale>) {
        donnees = newDonnees
        notifyDataSetChanged()
    }
} 