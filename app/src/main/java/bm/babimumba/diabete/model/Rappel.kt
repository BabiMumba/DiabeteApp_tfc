package bm.babimumba.diabete.model

data class Rappel(
    val id: String = "",
    val patientId: String = "",
    val titre: String = "",
    val description: String? = null,
    val dateHeure: String = "",
    val estActif: Boolean = true
)