package bm.babimumba.diabete.utils

import android.util.Log
import bm.babimumba.diabete.model.DonneeMedicale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Gestionnaire d'intégrité des données médicales
 * Coordonne le hachage et l'enregistrement sur la blockchain
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
     * @param onSuccess Callback appelé en cas de succès
     * @param onError Callback appelé en cas d'erreur
     */
    fun processMedicalData(
        donnee: DonneeMedicale,
        onSuccess: (DonneeMedicale) -> Unit,
        onError: (String) -> Unit
    ) {
        scope.launch {
            try {
                // 1. Générer le hash de la donnée
                val timestamp = HashUtils.generateTimestamp()
                val hash = HashUtils.generateMedicalDataHash(
                    patientId = donnee.patientId,
                    dateHeure = donnee.dateHeure,
                    glycemie = donnee.glycemie,
                    repas = donnee.repas,
                    insuline = donnee.insuline,
                    activite = donnee.activite,
                    commentaire = donnee.commentaire,
                    timestamp = timestamp
                )
                
                Log.d(TAG, "Hash généré pour la donnée: $hash")
                
                // 2. Préparer les métadonnées pour la blockchain
                val metadata = mapOf(
                    "patientId" to donnee.patientId,
                    "dateHeure" to donnee.dateHeure,
                    "timestamp" to timestamp.toString(),
                    "dataType" to "medical_data",
                    "source" to donnee.source
                )
                
                // 3. Enregistrer le hash sur la blockchain
                val blockchainSuccess = blockchainService.registerHashOnBlockchain(hash, metadata)
                
                if (blockchainSuccess) {
                    // 4. Créer la donnée avec intégrité
                    val donneeWithIntegrity = donnee.copy(
                        id = donnee.id.ifEmpty { generateId() },
                        // Ajouter les champs d'intégrité
                        // Note: Vous devrez modifier le modèle DonneeMedicale
                    )
                    
                    Log.d(TAG, "Donnée médicale traitée avec succès, hash: $hash")
                    
                    // 5. Retourner le succès
                    withContext(Dispatchers.Main) {
                        onSuccess(donneeWithIntegrity)
                    }
                } else {
                    throw Exception("Échec de l'enregistrement sur la blockchain")
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Erreur lors du traitement de la donnée médicale", e)
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
                // 1. Régénérer le hash de la donnée
                val timestamp = HashUtils.generateTimestamp()
                val expectedHash = HashUtils.generateMedicalDataHash(
                    patientId = donnee.patientId,
                    dateHeure = donnee.dateHeure,
                    glycemie = donnee.glycemie,
                    repas = donnee.repas,
                    insuline = donnee.insuline,
                    activite = donnee.activite,
                    commentaire = donnee.commentaire,
                    timestamp = timestamp
                )
                
                // 2. Vérifier sur la blockchain
                val blockchainVerified = blockchainService.verifyHashOnBlockchain(expectedHash)
                
                Log.d(TAG, "Vérification d'intégrité: blockchain=$blockchainVerified, hash=$expectedHash")
                
                withContext(Dispatchers.Main) {
                    onResult(blockchainVerified, expectedHash)
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Erreur lors de la vérification d'intégrité", e)
                withContext(Dispatchers.Main) {
                    onResult(false, null)
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
                val results = mutableMapOf<String, Boolean>()
                
                donnees.forEach { donnee ->
                    val timestamp = HashUtils.generateTimestamp()
                    val hash = HashUtils.generateMedicalDataHash(
                        patientId = donnee.patientId,
                        dateHeure = donnee.dateHeure,
                        glycemie = donnee.glycemie,
                        repas = donnee.repas,
                        insuline = donnee.insuline,
                        activite = donnee.activite,
                        commentaire = donnee.commentaire,
                        timestamp = timestamp
                    )
                    
                    val verified = blockchainService.verifyHashOnBlockchain(hash)
                    results[donnee.id] = verified
                }
                
                Log.d(TAG, "Vérification multiple terminée: ${results.size} données vérifiées")
                
                withContext(Dispatchers.Main) {
                    onResult(results)
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Erreur lors de la vérification multiple", e)
                withContext(Dispatchers.Main) {
                    onResult(emptyMap())
                }
            }
        }
    }
    
    /**
     * Génère un ID unique pour une donnée
     */
    private fun generateId(): String {
        return "data_${System.currentTimeMillis()}_${(0..9999).random()}"
    }
} 