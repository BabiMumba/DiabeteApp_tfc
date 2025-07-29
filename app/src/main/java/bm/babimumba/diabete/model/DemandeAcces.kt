package bm.babimumba.diabete.model

data class DemandeAcces(
    val id: String = "",
    val patientId: String = "",
    val medecinId: String = "",
    val nomMedecin: String = "",
    val emailMedecin: String = "",
    val specialiteMedecin: String = "",
    val hopitalMedecin: String = "",
    val dateHeureDemande: String = "",
    val statut: String = "en_attente" // "en_attente", "accepte", "refuse"
) 