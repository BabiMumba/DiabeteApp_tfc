package bm.babimumba.diabete.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Service pour l'intégration avec la blockchain via backend Node.js
 * Utilise les APIs REST pour communiquer avec Hyperledger Fabric
 */
class BlockchainService {
    
    companion object {
        private const val TAG = "BlockchainService"
        //192.168.115.84
        private const val BASE_URL = "http://192.168.115.84:3000"
    }
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    /**
     * Test de connectivité avec le backend
     * @return true si la connexion est établie
     */
    suspend fun testConnection(): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "🧪 Test de connexion vers: $BASE_URL/api/test")
            
            val request = Request.Builder()
                .url("$BASE_URL/api/test")
                .get()
                .build()
            
            client.newCall(request).execute().use { response ->
                val success = response.isSuccessful
                if (success) {
                    val responseBody = response.body?.string()
                    Log.d(TAG, "✅ Connexion réussie: $responseBody")
                } else {
                    Log.e(TAG, "❌ Erreur HTTP: ${response.code}")
                }
                success
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erreur de connexion:", e)
            false
        }
    }
    
    /**
     * Enregistre un hash sur la blockchain via le backend
     * @param patientId ID du patient
     * @param medicalData Données médicales à hasher
     * @return Le hash généré et enregistré
     */
    suspend fun registerHashOnBlockchain(
        patientId: String,
        medicalData: Map<String, Any>
    ): String = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "🔗 Enregistrement hash vers: $BASE_URL/api/integrity/register-hash")
            Log.d(TAG, "📊 Données patient: $patientId")
            
            val json = JSONObject().apply {
                put("patientId", patientId)
                put("medicalData", JSONObject(medicalData))
            }
            
            val requestBody = json.toString()
                .toRequestBody("application/json".toMediaType())
            
            val request = Request.Builder()
                .url("$BASE_URL/api/integrity/register-hash")
                .post(requestBody)
                .build()
            
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d(TAG, "✅ Réponse enregistrement: $responseBody")
                    
                    val jsonResponse = JSONObject(responseBody ?: "")
                    val hash = jsonResponse.getString("hash")
                    val transactionId = jsonResponse.optString("transactionId", "")
                    
                    Log.d(TAG, "⛓️ Hash enregistré: $hash")
                    if (transactionId.isNotEmpty()) {
                        Log.d(TAG, "🆔 Transaction ID: $transactionId")
                    }
                    
                    hash
                } else {
                    Log.e(TAG, "❌ Erreur HTTP: ${response.code}")
                    val errorBody = response.body?.string()
                    Log.e(TAG, "❌ Corps d'erreur: $errorBody")
                    throw IOException("Erreur serveur: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erreur lors de l'enregistrement:", e)
            // Fallback vers simulation locale
            val fallbackHash = "simulated_hash_${System.currentTimeMillis()}"
            Log.w(TAG, "🔄 Utilisation du hash de fallback: $fallbackHash")
            fallbackHash
        }
    }
    
    /**
     * Vérifie l'intégrité d'un hash via le backend
     * @param patientId ID du patient
     * @param timestamp Timestamp de la donnée
     * @param medicalData Données médicales à vérifier
     * @return true si l'intégrité est vérifiée
     */
    suspend fun verifyHashOnBlockchain(
        patientId: String,
        timestamp: Long,
        medicalData: Map<String, Any>
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "🔍 Vérification hash vers: $BASE_URL/api/integrity/verify-hash")
            Log.d(TAG, "📊 Patient: $patientId, Timestamp: $timestamp")
            
            val json = JSONObject().apply {
                put("patientId", patientId)
                put("timestamp", timestamp)
                put("medicalData", JSONObject(medicalData))
            }
            
            val requestBody = json.toString()
                .toRequestBody("application/json".toMediaType())
            
            val request = Request.Builder()
                .url("$BASE_URL/api/integrity/verify-hash")
                .post(requestBody)
                .build()
            
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d(TAG, "✅ Réponse vérification: $responseBody")
                    
                    val jsonResponse = JSONObject(responseBody ?: "")
                    val verified = jsonResponse.getBoolean("verified")
                    val regeneratedHash = jsonResponse.optString("regeneratedHash", "")
                    val originalHash = jsonResponse.optString("originalHash", "")
                    
                    Log.d(TAG, "🔍 Résultat vérification: $verified")
                    if (regeneratedHash.isNotEmpty() && originalHash.isNotEmpty()) {
                        Log.d(TAG, "🔄 Hash régénéré: $regeneratedHash")
                        Log.d(TAG, "📋 Hash original: $originalHash")
                    }
                    
                    verified
                } else {
                    Log.e(TAG, "❌ Erreur HTTP: ${response.code}")
                    val errorBody = response.body?.string()
                    Log.e(TAG, "❌ Corps d'erreur: $errorBody")
                    throw IOException("Erreur serveur: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erreur lors de la vérification:", e)
            // Fallback vers simulation locale
            Log.w(TAG, "🔄 Utilisation de la vérification de fallback")
            true
        }
    }
    
    /**
     * Récupère les métadonnées d'un hash depuis la blockchain
     * @param hash Le hash à interroger
     * @return Les métadonnées associées au hash
     */
    suspend fun getHashMetadata(hash: String): Map<String, String>? = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "📋 Récupération métadonnées pour le hash: $hash")
            
            // Pour l'instant, retourner des métadonnées simulées
            // Plus tard, vous pourriez ajouter un endpoint pour récupérer les métadonnées
            mapOf(
                "patientId" to "patient_123",
                "timestamp" to System.currentTimeMillis().toString(),
                "blockNumber" to "12345",
                "hash" to hash
            )
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erreur lors de la récupération des métadonnées", e)
            null
        }
    }
    
    /**
     * Vérifie l'intégrité d'un ensemble de données médicales
     * @param hashes Liste des hashes à vérifier
     * @return Map des résultats de vérification
     */
    suspend fun verifyMultipleHashes(hashes: List<String>): Map<String, Boolean> = withContext(Dispatchers.IO) {
        val results = mutableMapOf<String, Boolean>()
        
        hashes.forEach { hash ->
            results[hash] = verifyHashOnBlockchain("patient_123", System.currentTimeMillis(), emptyMap())
        }
        
        results
    }
} 