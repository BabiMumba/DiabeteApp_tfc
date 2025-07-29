package bm.babimumba.diabete.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import bm.babimumba.diabete.auth.LoginActivity
import bm.babimumba.diabete.databinding.FragmentProfilMedecinBinding
import bm.babimumba.diabete.model.Medecin
import bm.babimumba.diabete.utils.Constant
import bm.babimumba.diabete.utils.RoleManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfilMedecinFragment : Fragment() {
    private lateinit var binding: FragmentProfilMedecinBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilMedecinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMedecinProfile()
        setupButtons()
    }

    private fun loadMedecinProfile() {
        val medecinId = auth.currentUser?.uid
        if (medecinId != null) {
            binding.progressBar.visibility = View.VISIBLE

            db.collection(Constant.USER_COLLECTION)
                .document(medecinId)
                .get()
                .addOnSuccessListener { document ->
                    binding.progressBar.visibility = View.GONE
                    if (document.exists()) {
                        val medecin = document.toObject(Medecin::class.java)
                        medecin?.let {
                            displayMedecinInfo(it)
                        }
                    } else {
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = "Profil non trouvé"
                    }
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = "Erreur: ${e.message}"
                }
        }
    }

    private fun displayMedecinInfo(medecin: Medecin) {
        binding.tvNomMedecin.text = "${medecin.nom} ${medecin.prenom}"
        binding.tvEmailMedecin.text = medecin.email
        binding.tvSpecialite.text = medecin.specialite
        binding.tvNumeroOrdre.text = medecin.numeroOrdre
        binding.tvHopital.text = medecin.hopital
        binding.tvAdresse.text = medecin.adresse
        binding.tvTelephone.text = medecin.telephone
        binding.tvDateInscription.text = medecin.dateInscription
    }

    private fun setupButtons() {
        binding.btnDeconnexion.setOnClickListener {
            // Déconnexion
            auth.signOut()
            RoleManager.clearUserData(requireContext())
            
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnModifierProfil.setOnClickListener {
            // TODO: Implémenter la modification du profil
            Toast.makeText(requireContext(), "Fonctionnalité à venir", Toast.LENGTH_SHORT).show()
        }
    }
} 