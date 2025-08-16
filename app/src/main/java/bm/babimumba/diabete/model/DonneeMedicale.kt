package bm.babimumba.diabete.model

data class DonneeMedicale(
    var id: String = "",
    val patientId: String = "",
    val medecinId: String? = null, // ID du médecin si la donnée vient d'un médecin
    val dateHeure: String = "",
    val glycemie: String = "", // String pour gestion saisie utilisateur
    val repas: String? = null,
    val insuline: String? = null, // String pour gestion saisie utilisateur
    val activite: String? = null,
    val commentaire: String? = null,
    val mesureValide: Boolean = true,
    val source: String = "patient",
    // Champs d'intégrité blockchain
    val integrityHash: String? = null,
    val blockchainTimestamp: Long? = null,
    val blockchainVerified: Boolean = false,
    val dataIntegrityStatus: String = "pending" // pending, verified, failed
)
