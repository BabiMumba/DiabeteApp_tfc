package bm.babimumba.diabete.model

data class Partage(
    val id: String = "",
    val patientId: String = "",
    val medecinId: String = "",
    val dateHeurePartage: String = "",

    val statut: String = "actif"
)