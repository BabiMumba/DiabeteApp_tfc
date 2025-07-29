package bm.babimumba.diabete.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import bm.babimumba.diabete.R
import bm.babimumba.diabete.adapter.MedicamentAdapter
import bm.babimumba.diabete.databinding.ActivityDetailPrescriptionBinding
import bm.babimumba.diabete.model.Medicament
import bm.babimumba.diabete.model.Medecin
import bm.babimumba.diabete.model.Patient
import bm.babimumba.diabete.model.Prescription
import bm.babimumba.diabete.utils.Constant
import bm.babimumba.diabete.utils.DateUtils
import bm.babimumba.diabete.utils.RoleManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Cell
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DetailPrescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPrescriptionBinding
    private lateinit var medicamentAdapter: MedicamentAdapter
    private val medicaments = mutableListOf<Medicament>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var prescription: Prescription? = null
    private var medecin: Medecin? = null
    private var patient: Patient? = null
    private var isMedecin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPrescriptionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        // Vérifier le rôle de l'utilisateur
        isMedecin = RoleManager.getUserRole(this) == "medecin"

        setupToolbar()
        setupRecyclerView()
        loadPrescriptionDetails()
        setupButtons()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        medicamentAdapter = MedicamentAdapter(medicaments)
        binding.recyclerViewMedicaments.apply {
            layoutManager = LinearLayoutManager(this@DetailPrescriptionActivity)
            adapter = medicamentAdapter
        }
    }

    private fun setupButtons() {
        // Afficher/masquer les boutons selon le rôle
        if (isMedecin) {
            binding.btnModifier.visibility = View.VISIBLE
            binding.btnExporter.visibility = View.VISIBLE
            
            binding.btnModifier.setOnClickListener {
                // TODO: Implémenter la modification de prescription
                Toast.makeText(this, "Fonctionnalité à venir", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Patient : masquer le bouton modifier, garder l'export
            binding.btnModifier.visibility = View.GONE
            binding.btnExporter.visibility = View.VISIBLE
        }

        binding.btnExporter.setOnClickListener {
            exporterPrescriptionEnPdf()
        }
    }

    private fun exporterPrescriptionEnPdf() {
        if (prescription == null || medecin == null || patient == null) {
            Toast.makeText(this, "Données incomplètes pour l'export", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Créer le dossier si nécessaire
            val dir = getExternalFilesDir(null) ?: filesDir
            if (!dir.exists()) {
                dir.mkdirs()
            }

            // Nom du fichier
            val fileName = "Prescription_${prescription!!.id}_${System.currentTimeMillis()}.pdf"
            val file = File(dir, fileName)

            // Créer le PDF avec iText7
            val writer = PdfWriter(file)
            val pdf = PdfDocument(writer)
            val document = Document(pdf, PageSize.A4)
            document.setMargins(20f, 20f, 20f, 20f)

            // En-tête institutionnelle
            val entete = """
                REPUBLIQUE DEMOCRATIQUE DU CONGO
                Ministère de la Santé Publique, Hygiène Prévoyance Sociale
                PROVINCE DU HAUT KATANGA
                HOPITAL PUBLIC DE REFERENCE TERTIAIRE JASON SENDWE
                Lubumbashi

                PRESCRIPTION MÉDICALE No. ${prescription!!.id}
            """.trimIndent()
            
            document.add(
                Paragraph(entete)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12f)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE)
                    .setMarginBottom(20f)
            )

            // Informations du patient
            document.add(Paragraph("INFORMATIONS DU PATIENT").setBold().setFontSize(14f).setMarginBottom(10f))
            document.add(Paragraph("Nom : ${patient!!.name} ${patient!!.postnom}"))
            document.add(Paragraph("Âge : ${DateUtils.calculateAge(patient!!.date_naissance)} ans"))
            document.add(Paragraph("Sexe : ${patient!!.sexe}"))
            document.add(Paragraph("Type de diabète : ${patient!!.type_diabete}"))
            document.add(Paragraph(" ")) // Espace

            // Informations du médecin
            document.add(Paragraph("MÉDECIN PRESCRIPTEUR").setBold().setFontSize(14f).setMarginBottom(10f))
            document.add(Paragraph("Dr. ${medecin!!.nom} ${medecin!!.prenom}"))
            document.add(Paragraph("Spécialité : ${medecin!!.specialite}"))
            document.add(Paragraph("Hôpital : ${medecin!!.hopital}"))
            document.add(Paragraph(" ")) // Espace

            // Détails de la prescription
            document.add(Paragraph("DÉTAILS DE LA PRESCRIPTION").setBold().setFontSize(14f).setMarginBottom(10f))
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            document.add(Paragraph("Date de prescription : ${sdf.format(prescription!!.datePrescription)}"))
            document.add(Paragraph("Statut : ${prescription!!.statut}"))
            document.add(Paragraph("Durée du traitement : ${prescription!!.dureeTraitement}"))
            document.add(Paragraph(" ")) // Espace

            // Instructions générales
            if (prescription!!.instructions.isNotEmpty()) {
                document.add(Paragraph("INSTRUCTIONS GÉNÉRALES").setBold().setFontSize(14f).setMarginBottom(10f))
                document.add(Paragraph(prescription!!.instructions))
                document.add(Paragraph(" ")) // Espace
            }

            // Médicaments
            if (prescription!!.medicaments.isNotEmpty()) {
                document.add(Paragraph("MÉDICAMENTS PRESCRITS").setBold().setFontSize(14f).setMarginBottom(10f))
                
                val columnWidths = floatArrayOf(3f, 2f, 2f, 2f, 2f) // Nom, Dosage, Fréquence, Durée, Quantité
                val table = Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth()
                
                // En-têtes
                val headers = listOf("Médicament", "Dosage", "Fréquence", "Durée", "Quantité")
                headers.forEach { header ->
                    table.addHeaderCell(Cell().add(Paragraph(header)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER))
                }
                
                // Médicaments
                prescription!!.medicaments.forEach { medicament ->
                    table.addCell(Cell().add(Paragraph(medicament.nom)).setTextAlignment(TextAlignment.CENTER))
                    table.addCell(Cell().add(Paragraph(medicament.dosage)).setTextAlignment(TextAlignment.CENTER))
                    table.addCell(Cell().add(Paragraph(medicament.frequence)).setTextAlignment(TextAlignment.CENTER))
                    table.addCell(Cell().add(Paragraph(medicament.duree)).setTextAlignment(TextAlignment.CENTER))
                    table.addCell(Cell().add(Paragraph(medicament.quantite)).setTextAlignment(TextAlignment.CENTER))
                }
                
                document.add(table)
                document.add(Paragraph(" ")) // Espace
            }

            // Notes
            if (prescription!!.notes.isNotEmpty()) {
                document.add(Paragraph("NOTES").setBold().setFontSize(14f).setMarginBottom(10f))
                document.add(Paragraph(prescription!!.notes))
            }

            // Pied de page
            document.add(Paragraph(" ")) // Espace
            document.add(
                Paragraph("Signature du médecin : _________________")
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(30f)
            )

            document.close()
            writer.close()

            // Ouvrir le PDF
            val intent = Intent(this, PdfViewerActivity::class.java)
            intent.putExtra("pdf_path", file.absolutePath)
            startActivity(intent)

            Toast.makeText(this, "Prescription exportée avec succès", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e("DetailPrescriptionActivity", "Erreur export PDF: ${e.message}")
            Toast.makeText(this, "Erreur lors de l'export: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadPrescriptionDetails() {
        val prescriptionId = intent.getStringExtra("prescription_id")
        if (prescriptionId != null) {
            binding.progressBar.visibility = View.VISIBLE
            
            db.collection("prescriptions")
                .document(prescriptionId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        prescription = document.toObject(Prescription::class.java)
                        prescription?.let { pres ->
                            pres.id = document.id
                            displayPrescriptionInfo(pres)
                            loadMedecinInfo(pres.medecinId)
                            loadPatientInfo(pres.patientId)
                        }
                    } else {
                        Toast.makeText(this, "Prescription non trouvée", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    binding.progressBar.visibility = View.GONE
                }
                .addOnFailureListener { e ->
                    Log.e("DetailPrescriptionActivity", "Erreur lors du chargement: ${e.message}")
                    Toast.makeText(this, "Erreur lors du chargement", Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                    finish()
                }
        } else {
            Toast.makeText(this, "ID de prescription manquant", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun displayPrescriptionInfo(prescription: Prescription) {
        // Date de prescription
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.tvDatePrescription.text = "Date : ${sdf.format(prescription.datePrescription)}"
        
        // Statut
        val statutText = when (prescription.statut) {
            "active" -> "Active"
            "termine" -> "Terminée"
            "annule" -> "Annulée"
            else -> prescription.statut
        }
        binding.tvStatut.text = "Statut : $statutText"
        
        // Instructions
        binding.tvInstructions.text = prescription.instructions.ifEmpty { "Aucune instruction spécifique" }
        
        // Notes
        binding.tvNotes.text = prescription.notes.ifEmpty { "Aucune note" }
        
        // Médicaments
        medicaments.clear()
        medicaments.addAll(prescription.medicaments)
        medicamentAdapter.notifyDataSetChanged()
    }

    private fun loadMedecinInfo(medecinId: String) {
        db.collection(Constant.USER_COLLECTION)
            .document(medecinId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    medecin = document.toObject(Medecin::class.java)
                    medecin?.let { med ->
                        binding.tvNomMedecin.text = "Dr. ${med.nom} ${med.prenom}"
                        binding.tvSpecialiteMedecin.text = med.specialite
                        binding.tvHopitalMedecin.text = med.hopital
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("DetailPrescriptionActivity", "Erreur chargement médecin: ${e.message}")
            }
    }

    private fun loadPatientInfo(patientId: String) {
        db.collection(Constant.USER_COLLECTION)
            .document(patientId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    patient = document.toObject(Patient::class.java)
                    patient?.let { pat ->
                        binding.tvNomPatient.text = "${pat.name} ${pat.postnom}"
                        
                        // Calculer l'âge à partir de la date de naissance
                        val age = DateUtils.calculateAge(pat.date_naissance)
                        binding.tvAgePatient.text = "$age ans"
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("DetailPrescriptionActivity", "Erreur chargement patient: ${e.message}")
            }
    }
} 