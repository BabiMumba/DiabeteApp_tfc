package bm.babimumba.diabete.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.ActivityMainMedecinBinding
import bm.babimumba.diabete.activity.IntegrityCheckActivity
import bm.babimumba.diabete.docfragment.HomeMedecinFragment
import bm.babimumba.diabete.fragment.HistoriqueMedecinFragment
import bm.babimumba.diabete.fragment.ProfilMedecinFragment
import bm.babimumba.diabete.utils.VOID

class MainActivityMedecin : AppCompatActivity() {
    private lateinit var binding: ActivityMainMedecinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMedecinBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
        setupBottomNavigation()
        
        // Définir le fragment par défaut
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_home_medecin
            loadFragment(HomeMedecinFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_medecin -> {
                    binding.titreFr.text = "Mes Patients"
                    loadFragment(HomeMedecinFragment())
                    true
                }
                R.id.nav_historique_medecin -> {
                    binding.titreFr.text = "Prescriptions"
                    loadFragment(HistoriqueMedecinFragment())
                    true
                }
                R.id.nav_profil_medecin -> {
                    binding.titreFr.text = "Profil"
                    loadFragment(ProfilMedecinFragment())
                    true
                }
                R.id.integrity_check -> {
                    // Lancer l'activité de vérification d'intégrité
                    VOID.Intent1(this, IntegrityCheckActivity::class.java)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_medecin, fragment)
            .commit()
    }
} 