package bm.babimumba.diabete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bm.babimumba.diabete.databinding.ItemDonneeMedicaleBinding
import bm.babimumba.diabete.model.DonneeMedicale
import bm.babimumba.diabete.utils.Constant
import java.text.SimpleDateFormat
import java.util.*

class DonneeMedicaleAdapter(
    private var donnees: List<DonneeMedicale>,
    private val onItemClick: ((DonneeMedicale) -> Unit)? = null
) : RecyclerView.Adapter<DonneeMedicaleAdapter.DonneeViewHolder>() {
    
    // Cache pour les noms des médecins
    private val medecinNamesCache = mutableMapOf<String, String>()

    inner class DonneeViewHolder(private val binding: ItemDonneeMedicaleBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(donnee: DonneeMedicale) {
            // Ajouter le clic sur l'élément
            itemView.setOnClickListener {
                onItemClick?.invoke(donnee)
            }
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
            
            // Source avec nom du médecin si applicable
            if (donnee.source == "medecin" && !donnee.medecinId.isNullOrEmpty()) {
                val medecinId = donnee.medecinId
                
                // Vérifier le cache d'abord
                if (medecinNamesCache.containsKey(medecinId)) {
                    val nomCache = medecinNamesCache[medecinId]
                    if (!nomCache.isNullOrEmpty()) {
                        binding.tvSource.text = "Source : Dr. $nomCache"
                    } else {
                        binding.tvSource.text = "Source : Médecin"
                    }
                } else {
                    // Récupérer le nom du médecin depuis Firestore
                    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    db.collection(Constant.USER_COLLECTION)
                        .document(medecinId)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                // Essayer d'abord les champs pour les médecins (nom, prenom)
                                var nomMedecin = document.getString("nom") ?: ""
                                var postnomMedecin = document.getString("prenom") ?: ""
                                
                                // Si pas trouvé, essayer les champs pour les patients (name, postnom)
                                if (nomMedecin.isEmpty() && postnomMedecin.isEmpty()) {
                                    nomMedecin = document.getString("name") ?: ""
                                    postnomMedecin = document.getString("postnom") ?: ""
                                }
                                
                                val nomComplet = "$nomMedecin $postnomMedecin".trim()
                                
                                android.util.Log.d("DonneeMedicaleAdapter", "Médecin trouvé: nom=$nomMedecin, postnom=$postnomMedecin, complet=$nomComplet")
                                
                                if (nomComplet.isNotEmpty()) {
                                    // Mettre en cache
                                    medecinNamesCache[medecinId] = nomComplet
                                    
                                    // Mettre à jour l'affichage
                                    binding.tvSource.text = "Source : Dr. $nomComplet"
                                } else {
                                    binding.tvSource.text = "Source : Médecin"
                                }
                            } else {
                                android.util.Log.d("DonneeMedicaleAdapter", "Document médecin non trouvé pour ID: $medecinId")
                                binding.tvSource.text = "Source : Médecin"
                            }
                        }
                        .addOnFailureListener {
                            binding.tvSource.text = "Source : Médecin"
                        }
                }
            } else {
            binding.tvSource.text = "Source : ${donnee.source}"
            }
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