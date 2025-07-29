package bm.babimumba.diabete.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bm.babimumba.diabete.activity.CreerPrescriptionActivity
import bm.babimumba.diabete.activity.PatientDetailActivity
import bm.babimumba.diabete.databinding.ItemPatientMedecinBinding
import bm.babimumba.diabete.model.Patient
import bm.babimumba.diabete.utils.DateUtils

class PatientMedecinAdapter(
    private val patients: List<Patient>,
    private val onVoirDonneesClick: (Patient) -> Unit,
    private val onPrescrireClick: (Patient) -> Unit
) : RecyclerView.Adapter<PatientMedecinAdapter.PatientViewHolder>() {

    inner class PatientViewHolder(private val binding: ItemPatientMedecinBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(patient: Patient) {
            binding.tvNomPatient.text = "${patient.name} ${patient.postnom}"
            
            // Calculer l'âge à partir de la date de naissance
            val age = DateUtils.calculateAge(patient.date_naissance)
            binding.tvAgePatient.text = "$age ans"
            binding.tvEmailPatient.text = patient.email

            binding.btnVoirDonnees.setOnClickListener {
                onVoirDonneesClick(patient)
            }

            binding.btnPrescrire.setOnClickListener {
                onPrescrireClick(patient)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = ItemPatientMedecinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(patients[position])
    }

    override fun getItemCount(): Int = patients.size
} 