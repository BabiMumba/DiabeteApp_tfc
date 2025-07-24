package bm.babimumba.diabete.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bm.babimumba.diabete.R
import bm.babimumba.diabete.activity.PartageQrActivity
import bm.babimumba.diabete.activity.RappelActivity
import bm.babimumba.diabete.databinding.FragmentMenuBinding
import bm.babimumba.diabete.utils.VOID

class MenuFragment : Fragment() {

    lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding.rappel.setOnClickListener {
            VOID.Intent1(requireContext(), RappelActivity::class.java)
        }
        binding.partageqr.setOnClickListener {
            VOID.Intent1(requireContext(), PartageQrActivity::class.java)
        }
        return binding.root
    }

}