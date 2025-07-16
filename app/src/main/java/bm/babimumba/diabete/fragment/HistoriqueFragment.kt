package bm.babimumba.diabete.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bm.babimumba.diabete.R
import bm.babimumba.diabete.activity.AddMesureActivity
import bm.babimumba.diabete.activity.DetailActivity
import bm.babimumba.diabete.auth.LoginActivity
import bm.babimumba.diabete.databinding.FragmentHistoriqueBinding

class HistoriqueFragment : Fragment() {
    lateinit var binding: FragmentHistoriqueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoriqueBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding.btnAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddMesureActivity::class.java)
            startActivity(intent)
        }
  /*      binding.carte.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            startActivity(intent)
        }*/
        return binding.root
    }
}