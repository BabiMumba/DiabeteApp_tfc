package bm.babimumba.diabete

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import bm.babimumba.diabete.activity.RappelActivity
import bm.babimumba.diabete.databinding.ActivityMainBinding
import bm.babimumba.diabete.fragment.HistoriqueFragment
import bm.babimumba.diabete.fragment.HomeFragment
import bm.babimumba.diabete.fragment.MenuFragment
import bm.babimumba.diabete.utils.VOID

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
       // enableEdgeToEdge()
        setContentView(binding.root)
    /*    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        loadFragment(HomeFragment())
        inifragment()
        binding.bottomNavigationView.selectedItemId = R.id.home_fragment
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Vérifier si le fragment courant est HomeFragment
                val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment)
                if (currentFragment is HomeFragment) {
                    // Si c'est HomeFragment, on quitte l'application
                    finish()
                } else {
                    // Sinon, on retourne au fragment précédent
                    supportFragmentManager.popBackStack()

                }
            }
        })

        binding.menuBtn.setOnClickListener {
            // Ouvrir le menu
            if (binding.drawerLayout.isDrawerOpen(binding.navView)) {
                binding.drawerLayout.closeDrawer(binding.navView)
            } else {
                binding.drawerLayout.openDrawer(binding.navView)
            }
        }

        // Gestion des clics sur les éléments du menu
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            onNavigationItemSelected(menuItem.itemId)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun inifragment(){
        var homeFragment: HomeFragment? = null
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_fragment -> {
                    if (homeFragment == null) {
                        homeFragment = HomeFragment()
                    }
                    loadFragment(homeFragment!!)
                    return@setOnItemSelectedListener true
                }
                R.id.history -> {
                    // ShowToobar(true)
                    loadFragment(HistoriqueFragment())
                    return@setOnItemSelectedListener true

                }
                R.id.menuFr -> {
                    // ShowToobar(true)
                    loadFragment(MenuFragment())
                    return@setOnItemSelectedListener true

                }
            }
            false
        }

    }
    //clic sur le navigation view
    fun onNavigationItemSelected(itemId: Int) {
        when (itemId) {
            R.id.home_fragment -> {
                loadFragment(HomeFragment())
            }
            R.id.history -> {
                loadFragment(HistoriqueFragment())
            }
            R.id.rappel -> {
                VOID.Intent1(this, RappelActivity::class.java)
            }
        }
        binding.drawerLayout.closeDrawers() // Fermer le tiroir après la sélection
    }
}