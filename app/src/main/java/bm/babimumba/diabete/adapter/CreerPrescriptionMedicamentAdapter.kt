package bm.babimumba.diabete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bm.babimumba.diabete.databinding.ItemMedicamentCreerBinding
import bm.babimumba.diabete.model.Medicament

class CreerPrescriptionMedicamentAdapter(
    private val medicaments: MutableList<Medicament>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<CreerPrescriptionMedicamentAdapter.MedicamentViewHolder>() {

    inner class MedicamentViewHolder(val binding: ItemMedicamentCreerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicamentViewHolder {
        val binding = ItemMedicamentCreerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedicamentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicamentViewHolder, position: Int) {
        val medicament = medicaments[position]
        holder.binding.apply {
            tvNomMedicament.text = medicament.nom
            tvDosage.text = "Dosage : ${medicament.dosage}"
            tvFrequence.text = "Fréquence : ${medicament.frequence}"
            tvDuree.text = "Durée : ${medicament.duree}"
            tvQuantite.text = "Quantité : ${medicament.quantite}"
            
            if (medicament.instructions.isNotEmpty()) {
                tvInstructions.text = "Instructions : ${medicament.instructions}"
                tvInstructions.visibility = android.view.View.VISIBLE
            } else {
                tvInstructions.visibility = android.view.View.GONE
            }

            btnSupprimer.setOnClickListener {
                onDeleteClick(position)
            }
        }
    }

    override fun getItemCount(): Int = medicaments.size
} 