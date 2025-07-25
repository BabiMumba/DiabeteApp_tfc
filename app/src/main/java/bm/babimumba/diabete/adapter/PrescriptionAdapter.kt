package bm.babimumba.diabete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bm.babimumba.diabete.databinding.ItemPrescriptionBinding
import bm.babimumba.diabete.model.Prescription
import java.text.SimpleDateFormat
import java.util.*

class PrescriptionAdapter(
    private val prescriptions: List<Prescription>,
    private val onItemClick: (Prescription) -> Unit
) : RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    inner class PrescriptionViewHolder(private val binding: ItemPrescriptionBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(prescription: Prescription) {
            binding.tvDatePrescription.text = "Prescrit le ${dateFormat.format(prescription.datePrescription)}"
            binding.tvNombreMedicaments.text = "${prescription.medicaments.size} médicament(s)"
            binding.tvInstructions.text = prescription.instructions
            
            if (prescription.dureeTraitement.isNotEmpty()) {
                binding.tvDureeTraitement.text = "Durée: ${prescription.dureeTraitement}"
                binding.tvDureeTraitement.visibility = android.view.View.VISIBLE
            } else {
                binding.tvDureeTraitement.visibility = android.view.View.GONE
            }

            // Statut avec couleur
            when (prescription.statut) {
                "active" -> {
                    binding.tvStatut.text = "Active"
                    binding.tvStatut.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
                }
                "termine" -> {
                    binding.tvStatut.text = "Terminée"
                    binding.tvStatut.setTextColor(android.graphics.Color.parseColor("#FF9800"))
                }
                "annule" -> {
                    binding.tvStatut.text = "Annulée"
                    binding.tvStatut.setTextColor(android.graphics.Color.parseColor("#F44336"))
                }
            }

            binding.root.setOnClickListener {
                onItemClick(prescription)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionViewHolder {
        val binding = ItemPrescriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PrescriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrescriptionViewHolder, position: Int) {
        holder.bind(prescriptions[position])
    }

    override fun getItemCount(): Int = prescriptions.size
} 