package bm.babimumba.diabete.utils

import android.util.Log
import bm.babimumba.diabete.model.DonneeMedicale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Gestionnaire d'intégrité des données médicales
 * Coordonne le hachage et l'enregistrement sur la blockchain via le backend Node.js
 */
class IntegrityManager {
    
    companion object {
        private const val TAG = "IntegrityManager"
    }
    
    private val blockchainService = BlockchainService()
    private val scope = CoroutineScope(Dispatchers.IO)
    
    /**
     * Traite une nouvelle donnée médicale avec intégrité blockchain
     * @param donnee La donnée médicale à traiter
     * @param onSuccess Callback appelé en cas de succès avec le hash généré
     * @param onError Callback appelé en cas d'erreur
     */
    fun processMedicalData(
        donnee: DonneeMedicale,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        scope.launch {
            try {
                Log.d(TAG, "🔗 Début du traitement de la donnée médicale")
                
                // 1. Préparer les données médicales pour le backend
                val medicalData = mapOf<String, Any>(
                    "dateHeure" to (donnee.dateHeure ?: ""),
                    "glycemie" to (donnee.glycemie ?: ""),
                    "repas" to (donnee.repas ?: ""),
                    "insuline" to (donnee.insuline ?: ""),
                    "activite" to (donnee.activite ?: ""),
                    "commentaire" to (donnee.commentaire ?: ""),
                    "source" to (donnee.source ?: "")
                )
                
                // 2. Enregistrer le hash sur la blockchain via le backend
                val hash = blockchainService.registerHashOnBlockchain(
                    patientId = donnee.patientId,
                    medicalData = medicalData
                )
                
                Log.d(TAG, "✅ Hash enregistré avec succès: $hash")
                
                // 3. Retourner le succès avec le hash
                withContext(Dispatchers.Main) {
                    onSuccess(hash)
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Erreur lors du traitement de la donnée médicale", e)
                withContext(Dispatchers.Main) {
                    onError("Erreur d'intégrité: ${e.message}")
                }
            }
        }
    }
    
    /**
     * Vérifie l'intégrité d'une donnée médicale
     * @param donnee La donnée à vérifier
     * @param onResult Callback avec le résultat de la vérification
     */
    fun verifyMedicalDataIntegrity(
        donnee: DonneeMedicale,
        onResult: (Boolean, String?) -> Unit
    ) {
        scope.launch {
            try {
                Log.d(TAG, "🔍 Début de la vérification d'intégrité")
                
                // 1. Préparer les données médicales pour la vérification
                val medicalData = mapOf<String, Any>(
                    "dateHeure" to (donnee.dateHeure ?: ""),
                    "glycemie" to (donnee.glycemie ?: ""),
                    "repas" to (donnee.repas ?: ""),
                    "insuline" to (donnee.insuline ?: ""),
                    "activite" to (donnee.activite ?: ""),
                    "commentaire" to (donnee.commentaire ?: ""),
                    "source" to (donnee.source ?: "")
                )
                
                // 2. Vérifier sur la blockchain via le backend
                val timestamp = donnee.dateHeure.toLongOrNull() ?: System.currentTimeMillis()
                val blockchainVerified = blockchainService.verifyHashOnBlockchain(
                    patientId = donnee.patientId,
                    timestamp = timestamp,
                    medicalData = medicalData
                )
                
                Log.d(TAG, "🔍 Vérification d'intégrité terminée: $blockchainVerified")
                
                withContext(Dispatchers.Main) {
                    onResult(blockchainVerified, "Hash vérifié via blockchain")
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Erreur lors de la vérification d'intégrité", e)
                withContext(Dispatchers.Main) {
                    onResult(false, "Erreur: ${e.message}")
                }
            }
        }
    }
    
    /**
     * Vérifie l'intégrité de plusieurs données médicales
     * @param donnees Liste des données à vérifier
     * @param onResult Callback avec les résultats de vérification
     */
    fun verifyMultipleMedicalDataIntegrity(
        donnees: List<DonneeMedicale>,
        onResult: (Map<String, Boolean>) -> Unit
    ) {
        scope.launch {
            try {
                Log.d(TAG, "🔍 Début de la vérification multiple: ${donnees.size} données")
                
                val results = mutableMapOf<String, Boolean>()
                
                donnees.forEach { donnee ->
                    try {
                        val medicalData = mapOf<String, Any>(
                            "dateHeure" to (donnee.dateHeure ?: ""),
                            "glycemie" to (donnee.glycemie ?: ""),
                            "repas" to (donnee.repas ?: ""),
                            "insuline" to (donnee.insuline ?: ""),
                            "activite" to (donnee.activite ?: ""),
                            "commentaire" to (donnee.commentaire ?: ""),
                            "source" to (donnee.source ?: "")
                        )
                        
                        val timestamp = donnee.dateHeure.toLongOrNull() ?: System.currentTimeMillis()
                        val verified = blockchainService.verifyHashOnBlockchain(
                            patientId = donnee.patientId,
                            timestamp = timestamp,
                            medicalData = medicalData
                        )
                        
                        results[donnee.id] = verified
                        Log.d(TAG, "✅ Donnée ${donnee.id}: $verified")
                        
                    } catch (e: Exception) {
                        Log.e(TAG, "❌ Erreur pour la donnée ${donnee.id}:", e)
                        results[donnee.id] = false
                    }
                }
                
                Log.d(TAG, "🔍 Vérification multiple terminée: ${results.size} données vérifiées")
                
                withContext(Dispatchers.Main) {
                    onResult(results)
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Erreur lors de la vérification multiple", e)
                withContext(Dispatchers.Main) {
                    onResult(emptyMap())
                }
            }
        }
    }
    
    /**
     * Test de connectivité avec le backend
     * @param onResult Callback avec le résultat du test
     */
    fun testBackendConnection(onResult: (Boolean) -> Unit) {
        scope.launch {
            try {
                Log.d(TAG, "🧪 Test de connexion au backend")
                val isConnected = blockchainService.testConnection()
                
                Log.d(TAG, "🧪 Résultat du test: $isConnected")
                
                withContext(Dispatchers.Main) {
                    onResult(isConnected)
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Erreur lors du test de connexion", e)
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }
} 