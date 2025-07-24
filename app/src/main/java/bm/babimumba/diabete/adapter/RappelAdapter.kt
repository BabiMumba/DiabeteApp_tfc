package bm.babimumba.diabete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import bm.babimumba.diabete.databinding.ItemRappelBinding
import bm.babimumba.diabete.model.Rappel
import java.text.SimpleDateFormat

class RappelAdapter(
    private val items: List<Rappel>,
    private val onDelete: (Rappel) -> Unit,
    private val onActiveChanged: (Rappel, Boolean) -> Unit
) : RecyclerView.Adapter<RappelAdapter.RappelViewHolder>() {

    inner class RappelViewHolder(val binding: ItemRappelBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RappelViewHolder {
        val binding = ItemRappelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RappelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RappelViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val heure = sdf.format(item.timestamp)
            tvHeureRappel.text = heure.substring(11, 16) // Affiche l'heure au format HH:mm
            tvDateRappel.text = heure.substring(0, 10) // Affiche la date au format dd/MM/yyyy
            tvTypeRappel.text = item.type
            tvMessageRappel.text = item.message
            switchActive.isChecked = true // Par défaut actif
            switchActive.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                onActiveChanged(item, isChecked)
            }
            btnDeleteRappel.setOnClickListener {
                onDelete(item)
            }
        }
    }

    override fun getItemCount(): Int = items.size
} 