package bm.babimumba.diabete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bm.babimumba.diabete.databinding.ItemMedecinAccesBinding
import bm.babimumba.diabete.model.MedecinAcces

class MedecinAccesAdapter(
    private val items: List<MedecinAcces>,
    private val onRevoquer: (MedecinAcces) -> Unit
) : RecyclerView.Adapter<MedecinAccesAdapter.MedecinViewHolder>() {

    inner class MedecinViewHolder(val binding: ItemMedecinAccesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedecinViewHolder {
        val binding = ItemMedecinAccesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedecinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedecinViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            tvNomMedecin.text = item.nom
            tvEmailMedecin.text = item.email
            btnRevoquer.setOnClickListener { onRevoquer(item) }
        }
    }

    override fun getItemCount(): Int = items.size
} 