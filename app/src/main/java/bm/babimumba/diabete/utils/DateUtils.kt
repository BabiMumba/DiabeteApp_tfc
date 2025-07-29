package bm.babimumba.diabete.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    
    /**
     * Calcule l'âge à partir d'une date de naissance en string
     * Gère différents formats de date
     */
    fun calculateAge(dateNaissance: String): String {
        return try {
            if (dateNaissance.isBlank()) {
                return "--"
            }
            
            // Essayer différents formats de date
            val formats = listOf(
                "dd/MM/yyyy",  // 13/01/1990
                "dd/M/yyyy",   // 13/1/1990
                "yyyy",        // 1990
                "dd/MM",       // 13/01
                "dd/M"         // 13/1
            )
            
            var parsedDate: Date? = null
            var usedFormat = ""
            
            for (format in formats) {
                try {
                    val sdf = SimpleDateFormat(format, Locale.getDefault())
                    parsedDate = sdf.parse(dateNaissance)
                    usedFormat = format
                    break
                } catch (e: Exception) {
                    continue
                }
            }
            
            if (parsedDate != null) {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val calendar = Calendar.getInstance()
                calendar.time = parsedDate
                val birthYear = calendar.get(Calendar.YEAR)
                
                // Si le format ne contient pas l'année, retourner "--"
                if (usedFormat.contains("yyyy")) {
                    (currentYear - birthYear).toString()
                } else {
                    "--"
                }
            } else {
                "--"
            }
        } catch (e: Exception) {
            "--"
        }
    }
    
    /**
     * Formate une date au format dd/MM/yyyy
     */
    fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(date)
    }
    
    /**
     * Formate une date au format dd/MM/yyyy HH:mm
     */
    fun formatDateTime(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(date)
    }
} 