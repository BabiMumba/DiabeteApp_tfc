package bm.babimumba.diabete.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

object HashUtils {
    
    /**
     * Génère un hash SHA-256 d'une chaîne de données
     */
    fun generateHash(data: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(data.toByteArray())
            bytesToHex(hashBytes)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Algorithme SHA-256 non disponible", e)
        }
    }
    
    /**
     * Génère un hash pour une donnée médicale
     */
    fun generateMedicalDataHash(
        patientId: String,
        dateHeure: String,
        glycemie: String,
        repas: String?,
        insuline: String?,
        activite: String?,
        commentaire: String?,
        timestamp: Long
    ): String {
        val dataString = buildString {
            append(patientId)
            append("|")
            append(dateHeure)
            append("|")
            append(glycemie)
            append("|")
            append(repas ?: "")
            append("|")
            append(insuline ?: "")
            append("|")
            append(activite ?: "")
            append("|")
            append(commentaire ?: "")
            append("|")
            append(timestamp)
        }
        return generateHash(dataString)
    }
    
    /**
     * Vérifie l'intégrité d'une donnée médicale
     */
    fun verifyDataIntegrity(
        data: String,
        expectedHash: String
    ): Boolean {
        val actualHash = generateHash(data)
        return actualHash == expectedHash
    }
    
    /**
     * Convertit un tableau de bytes en hexadécimal
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = "0123456789abcdef"[v ushr 4]
            hexChars[i * 2 + 1] = "0123456789abcdef"[v and 0x0F]
        }
        return String(hexChars)
    }
    
    /**
     * Génère un timestamp unique
     */
    fun generateTimestamp(): Long {
        return System.currentTimeMillis()
    }
} 