package bm.babimumba.diabete.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bm.babimumba.diabete.databinding.ItemMedicamentBinding
import bm.babimumba.diabete.model.Medicament

class MedicamentAdapter(
    private val medicaments: List<Medicament>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<MedicamentAdapter.MedicamentViewHolder>() {

    inner class MedicamentViewHolder(private val binding: ItemMedicamentBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(medicament: Medicament, position: Int) {
            binding.tvNomMedicament.text = medicament.nom
            binding.tvDosage.text = medicament.dosage
            binding.tvFrequence.text = medicament.frequence
            binding.tvDuree.text = medicament.duree
            binding.tvQuantite.text = medicament.quantite
            
            if (medicament.instructions.isNotEmpty()) {
                binding.tvInstructions.text = medicament.instructions
                binding.tvInstructions.visibility = View.VISIBLE
            } else {
                binding.tvInstructions.visibility = View.GONE
            }

            binding.btnSupprimer.setOnClickListener {
                onDeleteClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicamentViewHolder {
        val binding = ItemMedicamentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedicamentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicamentViewHolder, position: Int) {
        holder.bind(medicaments[position], position)
    }

    override fun getItemCount(): Int = medicaments.size
} 