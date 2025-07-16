package bm.babimumba.diabete.model

data class Message(
    val id: String = "",
    val fromId: String = "",
    val toId: String = "",
    val contenu: String = "",
    val timestamp: Long = System.currentTimeMillis()
)