package bm.babimumba.diabete.model

import java.util.Date

data class Prescription(
    var id: String = "",
    val patientId: String = "",
    val medecinId: String = "",
    val datePrescription: Date = Date(),
    val medicaments: List<Medicament> = emptyList(),
    val instructions: String = "",
    val dureeTraitement: String = "",
    val statut: String = "active", // active, termine, annule
    val notes: String = "",
    val dateFin: Date? = null
)

data class Medicament(
    val nom: String = "",
    val dosage: String = "",
    val frequence: String = "",
    val duree: String = "",
    val instructions: String = "",
    val quantite: String = ""
) 