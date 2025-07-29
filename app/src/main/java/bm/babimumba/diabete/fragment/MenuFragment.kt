package bm.babimumba.diabete.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bm.babimumba.diabete.R
import bm.babimumba.diabete.activity.DemandesAccesActivity
import bm.babimumba.diabete.activity.ExportDataActivity
import bm.babimumba.diabete.activity.IntegrityCheckActivity
import bm.babimumba.diabete.activity.MesPrescriptionsActivity
import bm.babimumba.diabete.activity.PartageQrActivity
import bm.babimumba.diabete.activity.RappelActivity
import bm.babimumba.diabete.activity.SplashScreen
import bm.babimumba.diabete.auth.LoginActivity
import bm.babimumba.diabete.databinding.FragmentMenuBinding
import bm.babimumba.diabete.utils.VOID
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

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
        binding.demandesAcces.setOnClickListener {
            VOID.Intent1(requireContext(), DemandesAccesActivity::class.java)
        }
        binding.exporterLyt.setOnClickListener {
            VOID.Intent1(requireContext(), ExportDataActivity::class.java)
        }
        binding.prescriptions.setOnClickListener {
            VOID.Intent1(requireContext(), MesPrescriptionsActivity::class.java)
        }
        binding.settingLyt.setOnClickListener {

        }
        binding.integrityCheck.setOnClickListener {
            VOID.Intent1(requireContext(), IntegrityCheckActivity::class.java)
        }
        binding.logout.setOnClickListener {
            logout()
        }
        return binding.root
    }

    fun logout(){
        val auth = Firebase.auth
        val alerdialog = AlertDialog.Builder(requireContext())
        alerdialog.setTitle("Déconnexion")
        alerdialog.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
        alerdialog.setPositiveButton("Oui") { dialog, which ->
            auth.signOut()
            VOID.Intent1(requireContext(), SplashScreen::class.java)
            requireActivity().finish()
        }
        alerdialog.setNegativeButton("Non") { dialog, which ->
            dialog.dismiss()
        }
        alerdialog.show()
    }


}