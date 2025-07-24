package bm.babimumba.diabete.activity

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TimePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import bm.babimumba.diabete.databinding.AddRappelBtsheetBinding
import bm.babimumba.diabete.model.Rappel
import java.util.Calendar
import java.text.SimpleDateFormat

class AddRappelBottomSheet(
    private val onRappelAdded: (Rappel) -> Unit
) : BottomSheetDialogFragment() {
    private var _binding: AddRappelBtsheetBinding? = null
    private val binding get() = _binding!!
    private var heure = 8
    private var minute = 0
    private var selectedTimestamp: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddRappelBtsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Type de rappel (spinner)
        val types = listOf("Glycémie", "Insuline", "Autre")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, types)
        binding.typeRappel.setAdapter(adapter)

        // TimePicker
/*        binding.tvDateTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val dialog = TimePickerDialog(requireContext(), { _: TimePicker, h: Int, m: Int ->
                heure = h
                minute = m
                binding.btnTimePicker.text = String.format("%02d:%02d", h, m)
            }, heure, minute, true)
            dialog.show()
        }*/

        // Date et heure (DatePicker + TimePicker)
        binding.btnDateTimePicker.setOnClickListener {
            val cal = Calendar.getInstance()
            val datePicker = android.app.DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val timePicker = android.app.TimePickerDialog(requireContext(), { _, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    cal.set(Calendar.SECOND, 0)
                    selectedTimestamp = cal.timeInMillis
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                    binding.tvDateTime.text = sdf.format(java.util.Date(selectedTimestamp!!))
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
                timePicker.show()
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        // Enregistrer
        binding.btnSaveRappel.setOnClickListener {
            if (selectedTimestamp == null) {
                binding.tvDateTime.error = "Veuillez choisir la date et l'heure"
                return@setOnClickListener
            }
            val type = binding.typeRappel.text.toString().ifEmpty { "Glycémie" }
            val message = binding.messageRappel.text.toString().ifEmpty { "N'oubliez pas votre $type !" }
            val repetition = binding.switchRepetition.isChecked
            val rappel = Rappel(
                id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                timestamp = selectedTimestamp!!,
                type = type,
                message = message,
                repetition = repetition
            )
            onRappelAdded(rappel)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 