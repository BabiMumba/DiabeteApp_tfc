package bm.babimumba.diabete.model

data class Rappel(
    val id: Int,
    val timestamp: Long, // date et heure complète du rappel
    val type: String,
    val message: String,
    val repetition: Boolean = false
) {
    override fun toString(): String {
        return "Rappel(id=$id, timestamp=$timestamp, type='$type', message='$message', repetition=$repetition)"
    }
    constructor() : this(
        id = 0,
        timestamp = System.currentTimeMillis(),
        type = "Glycémie",
        message = "N'oubliez pas votre glycémie !",
        repetition = false
    )
}