package bm.babimumba.diabete.model

data class Medecin(
    var id: String = "",
    val nom: String = "",
    val prenom: String = "",
    val email: String = "",
    val telephone: String = "",
    val specialite: String = "",
    val numeroOrdre: String = "",
    val hopital: String = "",
    val adresse: String = "",
    val dateInscription: String = "",
    val statut: String = "actif" // actif, inactif, suspendu
)