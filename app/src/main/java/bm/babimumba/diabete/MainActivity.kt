package bm.babimumba.diabete

import android.os.Bundle
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
// RappelActivity supprimé - seuls les médecins peuvent ajouter des rappels
import bm.babimumba.diabete.databinding.ActivityMainBinding
import bm.babimumba.diabete.fragment.HistoriqueFragment
import bm.babimumba.diabete.fragment.HomeFragment
import bm.babimumba.diabete.fragment.MenuFragment
import bm.babimumba.diabete.utils.Constant.PREF_KEY_USER_NAME
import bm.babimumba.diabete.utils.Constant.PREF_NAME
import bm.babimumba.diabete.utils.VOID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import bm.babimumba.diabete.utils.Constant

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
        loadname()
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
            // Rappel supprimé - seuls les médecins peuvent ajouter des rappels
        }
        binding.drawerLayout.closeDrawers() // Fermer le tiroir après la sélection
    }

    fun loadname(){
        val sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val userName = sharedPreferences.getString(PREF_KEY_USER_NAME, "")
        
        // Si le nom n'est pas dans les préférences, le récupérer depuis Firestore
        if (userName.isNullOrEmpty()) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val db = FirebaseFirestore.getInstance()
                db.collection(Constant.USER_COLLECTION)
                    .document(userId)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val patientName = document.getString("name") ?: ""
                            val patientPostnom = document.getString("postnom") ?: ""
                            val fullName = "$patientName $patientPostnom".trim()
                            
                            // Sauvegarder dans les préférences
                            val prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
                            prefs.edit().putString(PREF_KEY_USER_NAME, fullName).apply()
                            
                            // Afficher le nom
                            binding.tvName.text = fullName
                            binding.navView.getHeaderView(0).findViewById<TextView>(R.id.name_user).text = fullName
                        }
                    }
            }
        } else {
            binding.tvName.text = userName
            binding.navView.getHeaderView(0).findViewById<TextView>(R.id.name_user).text = userName
        }
    }
}