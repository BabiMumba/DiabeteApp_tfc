package bm.babimumba.diabete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bm.babimumba.diabete.databinding.ItemDemandeAccesBinding
import bm.babimumba.diabete.model.DemandeAcces

class DemandeAccesAdapter(
    private val items: List<DemandeAcces>,
    private val onAccepter: (DemandeAcces) -> Unit,
    private val onRefuser: (DemandeAcces) -> Unit
) : RecyclerView.Adapter<DemandeAccesAdapter.DemandeViewHolder>() {

    inner class DemandeViewHolder(val binding: ItemDemandeAccesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemandeViewHolder {
        val binding = ItemDemandeAccesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DemandeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DemandeViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            tvNomMedecin.text = "Dr. ${item.nomMedecin}"
            tvSpecialite.text = item.specialiteMedecin
            tvHopital.text = item.hopitalMedecin
            tvEmail.text = item.emailMedecin
            tvDateDemande.text = "Demande du ${item.dateHeureDemande}"
            
            btnAccepter.setOnClickListener { onAccepter(item) }
            btnRefuser.setOnClickListener { onRefuser(item) }
        }
    }

    override fun getItemCount(): Int = items.size
} 