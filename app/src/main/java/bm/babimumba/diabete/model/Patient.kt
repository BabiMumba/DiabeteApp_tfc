package bm.babimumba.diabete.model

data class Patient(
    val id: String = "",
    val nom: String = "",
    val postnom: String = "",
    val sexe : String = "",
    val dateNaissance: String = "",
    val poids: Double = 0.0,
    val typeDiabete: String = "TYPE_2",
    val email: String = "",
    val role: String = "patient"
)
