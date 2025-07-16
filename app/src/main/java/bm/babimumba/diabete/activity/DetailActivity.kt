package bm.babimumba.diabete.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize toolbar buttons
        setupToolbarButtons()
        
        // Initialize delete button
        setupDeleteButton()
    }
    
    private fun setupToolbarButtons() {
        
        // Edit button
        binding.btnEdit.setOnClickListener {
            Toast.makeText(this, "Modifier la mesure", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupDeleteButton() {
        binding.delete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }
    
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmer la suppression")
            .setMessage("Êtes-vous sûr de vouloir supprimer cette mesure ?")
            .setPositiveButton("Supprimer") { _, _ ->
                Toast.makeText(this, "Mesure supprimée", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
}