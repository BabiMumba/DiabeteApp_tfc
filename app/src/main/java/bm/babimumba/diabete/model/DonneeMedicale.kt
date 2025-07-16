package bm.babimumba.diabete.model

data class DonneeMedicale(
    val id: String = "",
    val patientId: String = "",
    val dateHeure: String = "",
    val glycemie: Double = 0.0,
    val repas: String? = null,
    val insuline: Double? = null,
    val activite: String? = null,
    val commentaire: String? = null,
    val mesureValide: Boolean = true,
    val source: String = "patient"
)
