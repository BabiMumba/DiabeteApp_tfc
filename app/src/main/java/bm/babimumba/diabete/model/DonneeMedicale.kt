package bm.babimumba.diabete.model

data class DonneeMedicale(
    var id: String = "",
    val patientId: String = "",
    val dateHeure: String = "",
    val glycemie: String = "", // String pour gestion saisie utilisateur
    val repas: String? = null,
    val insuline: String? = null, // String pour gestion saisie utilisateur
    val activite: String? = null,
    val commentaire: String? = null,
    val mesureValide: Boolean = true,
    val source: String = "patient"
)
