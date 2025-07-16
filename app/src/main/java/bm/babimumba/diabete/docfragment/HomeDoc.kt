package bm.babimumba.diabete.docfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.FragmentHomeDocBinding


class HomeDoc : Fragment() {
    lateinit var binding: FragmentHomeDocBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeDocBinding.inflate(inflater, container, false)

        return binding.root
    }

}