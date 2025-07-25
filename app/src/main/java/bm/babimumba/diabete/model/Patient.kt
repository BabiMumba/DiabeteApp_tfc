package bm.babimumba.diabete.model

data class Patient(
    var id: String = "",
    val name: String = "", // harmonisé avec le repository
    val postnom: String = "",
    val sexe: String = "",
    val date_naissance: String = "",
    val poids: String = "", // String pour correspondre à la saisie utilisateur
    val date_inscription: String = "",
    val type_diabete: String = "TYPE_2", // valeur fixe
    val email: String = "",
    val role: String = "patient"
)
