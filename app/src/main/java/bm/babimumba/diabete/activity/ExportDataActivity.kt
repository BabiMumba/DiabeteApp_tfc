package bm.babimumba.diabete.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import bm.babimumba.diabete.R
import bm.babimumba.diabete.databinding.ActivityExportDataBinding
import bm.babimumba.diabete.model.Patient
import bm.babimumba.diabete.model.DonneeMedicale
import bm.babimumba.diabete.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import android.widget.TableRow
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import android.os.Environment
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import com.itextpdf.layout.element.Text
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import bm.babimumba.diabete.utils.VOID
import com.itextpdf.layout.properties.HorizontalAlignment


class ExportDataActivity : AppCompatActivity() {
    lateinit var binding: ActivityExportDataBinding
    private val userRepository = UserRepository()
    private val userId: String by lazy { FirebaseAuth.getInstance().currentUser?.uid ?: "" }
    private var patient: Patient? = null
    private var mesures: List<DonneeMedicale> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityExportDataBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            //cela signifie que nous consommons les insets pour
            WindowInsetsCompat.CONSUMED
        }



        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Retour à l'activité précédente
        }
        binding.listExport.setOnClickListener {
            VOID.Intent1(this, ListExportActivity::class.java)
        }

        chargerInfosPatientEtMesures()

        binding.btnExportPdf.setOnClickListener {
            exporterEnPdf()
        }
    }

    private fun chargerInfosPatientEtMesures() {
        userRepository.getPatient(userId, onSuccess = { p ->
            patient = p
            userRepository.getDonneesMedicalesPatient(userId, onSuccess = { list ->
                mesures = list.sortedByDescending { it.dateHeure }
                remplirResumeEtTableau()
            }, onError = { showToast(it) })
        }, onError = { showToast(it) })
    }

    private fun remplirResumeEtTableau() {
        // Résumé patient
        val p = patient ?: return
        val nbMesures = mesures.size
        val valeursGlycemie = mesures.mapNotNull { it.glycemie.toDoubleOrNull() }
        val moyenne = if (valeursGlycemie.isNotEmpty()) valeursGlycemie.average() else 0.0
        val min = valeursGlycemie.minOrNull() ?: 0.0
        val max = valeursGlycemie.maxOrNull() ?: 0.0
        val totalInsuline = mesures.mapNotNull { it.insuline?.toDoubleOrNull() }.sum()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val resume = """
            Nom du Patient : ${p.name} ${p.postnom}
            Sexe : ${p.sexe}
            Date de naissance : ${p.date_naissance}
            Poids : ${p.poids} kg
            Type de diabète : ${p.type_diabete}
            Nombre d'enregistrements : $nbMesures
            Moyenne glycémie : ${"%.1f".format(moyenne)}
            Glycémie min : ${"%.1f".format(min)}
            Glycémie max : ${"%.1f".format(max)}
            Insuline totale utilisée : ${"%.1f".format(totalInsuline)}
        """.trimIndent()
        binding.tvResumePatient.text = resume

        // Tableau des mesures
        val table = binding.tableMesures
        // Supprimer les anciennes lignes sauf l'en-tête
        while (table.childCount > 1) table.removeViewAt(1)
        mesures.forEach { m ->
            val row = TableRow(this)
            val date = try { sdf.format(Date(m.dateHeure.toLong())) } catch (e: Exception) { m.dateHeure }
            val heure = try {
                val d = Date(m.dateHeure.toLong())
                SimpleDateFormat("HH:mm").format(d)
            } catch (e: Exception) { "" }
            val repas = m.repas ?: "-"
            val glycemie = m.glycemie
            val insuline = m.insuline ?: "-"
            val activite = m.activite ?: m.commentaire ?: "-"
            val cells = listOf(date, heure, repas, glycemie, insuline, "-", activite)
            cells.forEachIndexed { i, value ->
                val tv = TextView(this)
                tv.text = value
                tv.setTextAppearance(R.style.TableCell)
                row.addView(tv)
            }
            table.addView(row)
        }
    }
    private fun exporterEnPdf() {
        try {
            // 1. Configuration du fichier PDF
            val fileName = "FicheMedicale_${System.currentTimeMillis()}.pdf"
            val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: filesDir
            val file = File(dir, fileName)
            val writer = PdfWriter(FileOutputStream(file))
            val pdf = PdfDocument(writer)
            val document = Document(pdf, PageSize.A4)
            document.setMargins(20f, 20f, 20f, 20f)

            // 2. En-tête institutionnelle
            val entete = """
                REPUBLIQUE DEMOCRATIQUE DU CONGO
                Ministère de la Santé Publique, Hygiène Prévoyance Sociale
                PROVINCE DU HAUT KATANGA
               
            """.trimIndent()
            val hop_nam = "HOPITAL PUBLIC DE REFERENCE TERTIAIRE JASON SENDWE"
            val ville = "Lubumbashi"
            val num_fiche = "FICHE MEDICALE"


            document.add(
                Paragraph(entete)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12f)
                    .setBold()
                    .setFontColor(ColorConstants.BLACK)
                    .setMarginBottom(5f)
            )

            document.add(
                Paragraph(hop_nam)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(13f)
                    .setBold()
                    .setFontColor(ColorConstants.BLACK)
                    .setMarginBottom(5f)
            )
            document.add(
                Paragraph(ville)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(11f)
                    .setBold()
                    .setFontColor(ColorConstants.BLACK)
                    .setMarginBottom(5f)
            )

            document.add(
                Paragraph(num_fiche)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14f)
                    .setBold()
                    .setFontColor(ColorConstants.BLACK)
                    .setMarginBottom(5f)
            )
            val date_jour = SimpleDateFormat("dd/MM/yyyy").format(Date())
            document.add(
                Paragraph("Date : $date_jour Via Application Diabete")
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(10f)
                    .setBold()
                    .setFontColor(ColorConstants.RED)
                    .setMarginBottom(11f)
            )



            // 4. Résumé patient (labels gras, valeurs normales)
            val patient = patient ?: return
            val nbMesures = mesures.size
            val valeursGlycemie = mesures.mapNotNull { it.glycemie.toDoubleOrNull() }
            val moyenne = if (valeursGlycemie.isNotEmpty()) valeursGlycemie.average() else 0.0
            val min = valeursGlycemie.minOrNull() ?: 0.0
            val max = valeursGlycemie.maxOrNull() ?: 0.0
            val totalInsuline = mesures.mapNotNull { it.insuline?.toDoubleOrNull() }.sum()
            val sdf = SimpleDateFormat("dd/MM/yyyy")

            val resume = listOf(
                "Nom du Patient :" to "${patient.name} ${patient.postnom}",
                "Sexe :" to patient.sexe,
                "Date de naissance :" to patient.date_naissance,
                "Poids :" to "${patient.poids} kg",
                "Type de diabète :" to patient.type_diabete,
                "Nombre d'enregistrements :" to "$nbMesures",
                "Moyenne glycémie :" to "${"%.1f".format(moyenne)} mmol/L",
                "Glycémie minimale :" to "${"%.1f".format(min)} mmol/L",
                "Glycémie maximale :" to "${"%.1f".format(max)} mmol/L",
                "Insuline totale utilisée :" to "${"%.1f".format(totalInsuline)} unités"
            )
            resume.forEach { (label, value) ->
                val p = Paragraph().add(Text(label + " ").setBold()).add(Text(value))
                document.add(p)
            }
            document.add(Paragraph(" ")) // Espace

            // 5. Tableau stylé
            if (mesures.isNotEmpty()) {
                val columnWidths = floatArrayOf(2f, 1.5f, 2f, 2f, 3f) // Date, Heure, Glycémie, Insuline, Activité/Commentaire
                val table = Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth()

                // En-têtes du tableau
                val headers = listOf("Date", "Heure", "Glycémie (mmol/L)", "Insuline (UI)", "Activité / Commentaire")
                headers.forEach { header ->
                    table.addHeaderCell(
                        Cell().add(Paragraph(header).setFontSize(10f).setBold())
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    )
                }

                // Lignes de données
                val sdfHeure = SimpleDateFormat("HH:mm")
                mesures.forEach { m ->
                    val date = try { sdf.format(Date(m.dateHeure.toLong())) } catch (e: Exception) { m.dateHeure }
                    val heure = try { sdfHeure.format(Date(m.dateHeure.toLong())) } catch (e: Exception) { "" }
                    val glycemie = m.glycemie
                    val insuline = m.insuline ?: "-"
                    val activite = m.activite ?: m.commentaire ?: "-"
                    val rowData = listOf(date, heure, glycemie, insuline, activite)
                    rowData.forEach { data ->
                        table.addCell(
                            Cell().add(Paragraph(data).setFontSize(9f))
                                .setTextAlignment(TextAlignment.CENTER)
                        )
                    }
                }
                document.add(table)
            } else {
                document.add(Paragraph("Aucune mesure disponible.").setItalic())
            }

            document.close()
            writer.close()

            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Exportation terminée")
            dialogBuilder.setMessage("Le fichier PDF a été généré avec succès")
            //dialogBuilder.setIcon(R.drawable.logo)
            dialogBuilder.setPositiveButton("Ouvrir") { dialog, _ ->
                dialog.dismiss()
                // Ouvre le PDF
                val intent = Intent(this, PdfViewerActivity::class.java)
                intent.putExtra("pdf_path", file.absolutePath)
                startActivity(intent)
            }
            dialogBuilder.setNegativeButton("Fermer") { dialog, _ ->
                dialog.dismiss()
            }
            dialogBuilder.show()

        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Erreur lors de la génération du PDF: ${e.message}")
        }
    }






    private fun showToast(msg: String) {
        android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_SHORT).show()
    }
}