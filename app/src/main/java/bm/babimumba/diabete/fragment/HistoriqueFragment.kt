package bm.babimumba.diabete.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.R
import bm.babimumba.diabete.activity.AddMesureActivity
import bm.babimumba.diabete.activity.DetailActivity
import bm.babimumba.diabete.auth.LoginActivity
import bm.babimumba.diabete.databinding.FragmentHistoriqueBinding
import bm.babimumba.diabete.adapter.DonneeMedicaleAdapter
import bm.babimumba.diabete.utils.VOID
import bm.babimumba.diabete.viewmodel.HistoriqueViewModel
import bm.babimumba.diabete.viewmodel.HistoriqueState
import com.google.firebase.auth.FirebaseAuth

class HistoriqueFragment : Fragment() {
    lateinit var binding: FragmentHistoriqueBinding
    private val historiqueViewModel: HistoriqueViewModel by viewModels()
    private lateinit var adapter: DonneeMedicaleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoriqueBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment


        // Initialisation de l'adapter et du RecyclerView
        adapter = DonneeMedicaleAdapter(emptyList()) { donnee ->
            // Action lors du clic sur une donnée (navigation vers le détail)
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("donnee_id", donnee.id)
            startActivity(intent)
        }
        binding.recyclerViewMesures.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMesures.adapter = adapter

        // Observer l'état de l'historique
        historiqueViewModel.historiqueState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HistoriqueState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is HistoriqueState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.updateData(state.mesures)
                }
                is HistoriqueState.Error -> {
                    VOID.showSnackBar(binding.root,"Erreur lors du chargement de l'historique : ${state.message}")
                }
            }
        }

        // Charger l'historique du patient connecté
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            historiqueViewModel.chargerHistorique(userId)
        }

        return binding.root
    }

    override fun onResume() {
        //actualiser l'historique à chaque fois que le fragment est visible
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            historiqueViewModel.chargerHistorique(userId)
        }
        super.onResume()
    }
}