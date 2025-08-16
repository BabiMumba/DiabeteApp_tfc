package bm.babimumba.diabete.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bm.babimumba.diabete.R
import androidx.fragment.app.viewModels
import bm.babimumba.diabete.databinding.FragmentHomeBinding
import bm.babimumba.diabete.model.DonneeMedicale
import bm.babimumba.diabete.viewmodel.HomeViewModel
import bm.babimumba.diabete.viewmodel.HomeState
import com.google.firebase.auth.FirebaseAuth
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import android.graphics.Color
import bm.babimumba.diabete.activity.AddMesureActivity


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        // Observer l'état du Home
        homeViewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeState.Loading -> {
                    binding.circularLoader.root.visibility = View.VISIBLE
                }
                is HomeState.Success -> {
                    binding.circularLoader.root.visibility = View.GONE
                    afficherDerniereMesure(state.derniereMesure)
                }
                is HomeState.Error -> {
                    binding.circularLoader.root.visibility = View.GONE
                    Log.d("HomeFragment", "Erreur lors du chargement de la dernière mesure : ${state.message}")
                }
            }
        }

        // Charger la dernière mesure du patient connecté
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            homeViewModel.chargerDerniereMesure(userId)
            homeViewModel.chargerTendances(userId)
        }

        // Observer les tendances pour les graphiques
        homeViewModel.tendancesState.observe(viewLifecycleOwner) { mesures ->
            Log.d("HomeFragment", "Tendances reçues: ${mesures.size} mesures")
            mesures.forEach { mesure ->
                Log.d("HomeFragment", "Mesure: glycemie=${mesure.glycemie}, date=${mesure.dateHeure}")
            }
            if (mesures.isNotEmpty()) {
                Log.d("HomeFragment", "Configuration des graphiques avec ${mesures.size} mesures")
                configurerPieChart(mesures)
                configurerLineChart(mesures)
                afficherStatistiques(mesures)
            } else {
                Log.d("HomeFragment", "Aucune mesure pour les graphiques")
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Rafraîchir les données quand le fragment redevient visible
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            homeViewModel.chargerDerniereMesure(userId)
            homeViewModel.chargerTendances(userId)
        }
    }

    private fun afficherDerniereMesure(mesure: DonneeMedicale?) {
        Log.d("HomeFragment", "Dernière mesure récupérée : ${mesure?.glycemie}")
        if (mesure != null) {
            binding.tvDerniereGlycemie.text = "${mesure.glycemie} mg/dL"
            binding.tvDerniereDetails.text = mesure.commentaire ?: mesure.activite ?: "Pas de détails"
            binding.tvDerniereDate.text = formatTimestamp(mesure.dateHeure)
        } else {
            binding.tvDerniereGlycemie.text = "Aucune mesure"
            binding.tvDerniereDetails.text = "Votre médecin ajoutera vos mesures"
            binding.tvDerniereDate.text = ""
        }
    }

    private fun configurerPieChart(mesures: List<DonneeMedicale>) {
        val pieChart = binding.pieChart
        
        // Configuration du PieChart
        pieChart.description.isEnabled = false
        pieChart.setUsePercentValues(true)
        pieChart.legend.isEnabled = true
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.animateY(1000)
        
        // Calculer la répartition des valeurs

        var bas = 0
        var normale = 0
        var elevee = 0
        
        mesures.forEach { mesure ->
            val glycemie = mesure.glycemie.toDoubleOrNull() ?: 0.0
            when {
                glycemie < 70 -> bas++
                glycemie <= 140 -> normale++
                else -> elevee++
            }
        }
        
        val entries = mutableListOf<PieEntry>()
        if (bas > 0) entries.add(PieEntry(bas.toFloat(), "Bas"))
        if (normale > 0) entries.add(PieEntry(normale.toFloat(), "Normale"))
        if (elevee > 0) entries.add(PieEntry(elevee.toFloat(), "Élevé"))
        
        val dataSet = PieDataSet(entries, "Répartition glycémie")
        dataSet.colors = listOf(Color.rgb(255, 140, 0), Color.GREEN, Color.RED)
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = Color.WHITE
        
        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()
    }

    private fun configurerLineChart(mesures: List<DonneeMedicale>) {
        val lineChart = binding.lineChart
        
        // Configuration du LineChart
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = true
        lineChart.setTouchEnabled(true)
        lineChart.setDragEnabled(true)
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.animateX(1000)
        
        // Inverser l'ordre des données pour afficher de droite à gauche
        val mesuresInversees = mesures.reversed()
        
        // Série Glycémie
        val entriesGlycemie = mesuresInversees.mapIndexed { index, mesure ->
            Entry(index.toFloat(), mesure.glycemie.toFloatOrNull() ?: 0f)
        }
        val dataSetGlycemie = LineDataSet(entriesGlycemie, "Glycémie")
        dataSetGlycemie.color = Color.BLUE
        dataSetGlycemie.setCircleColor(Color.BLUE)
        dataSetGlycemie.lineWidth = 3f
        dataSetGlycemie.circleRadius = 5f
        dataSetGlycemie.valueTextSize = 12f

        // Série Insuline
        val entriesInsuline = mesuresInversees.mapIndexed { index, mesure ->
            Entry(index.toFloat(), mesure.insuline?.toFloatOrNull() ?: 0f)
        }
        val dataSetInsuline = LineDataSet(entriesInsuline, "Insuline")
        dataSetInsuline.color = Color.rgb(255, 140, 0) // Orange
        dataSetInsuline.setCircleColor(Color.rgb(255, 140, 0))
        dataSetInsuline.lineWidth = 3f
        dataSetInsuline.circleRadius = 5f
        dataSetInsuline.valueTextSize = 12f

        val data = LineData(dataSetGlycemie, dataSetInsuline)
        lineChart.data = data
        lineChart.invalidate()
    }

    private fun formatTimestamp(timestampStr: String?): String {
        return try {
            val timestamp = timestampStr?.toLongOrNull() ?: return ""
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
            val date = java.util.Date(timestamp)
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }
    private fun afficherStatistiques(mesures: List<DonneeMedicale>) {
        val (min, moyenne, max) = homeViewModel.calculerStatistiques(mesures)
        binding.tvMin.text = "Min: ${String.format("%.1f", min)}"
        binding.tvAvg.text = "Avg: ${String.format("%.1f", moyenne)}"
        binding.tvMax.text = "Max: ${String.format("%.1f", max)}"
    }
}