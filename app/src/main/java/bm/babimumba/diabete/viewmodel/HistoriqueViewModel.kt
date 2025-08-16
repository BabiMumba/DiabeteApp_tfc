package bm.babimumba.diabete.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bm.babimumba.diabete.model.DonneeMedicale
import bm.babimumba.diabete.repository.UserRepository

sealed class HistoriqueState {
    object Loading : HistoriqueState()
    data class Success(val mesures: List<DonneeMedicale>) : HistoriqueState()
    data class Error(val message: String) : HistoriqueState()
}

class HistoriqueViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {
    private val _historiqueState = MutableLiveData<HistoriqueState>()
    val historiqueState: LiveData<HistoriqueState> = _historiqueState

    fun chargerHistorique(patientId: String) {
        _historiqueState.value = HistoriqueState.Loading
        userRepository.getDonneesMedicalesPatient(
            patientId,
            onSuccess = { mesures ->
                // Trier les mesures par date (plus récent en premier)
                val mesuresTriees = mesures.sortedByDescending { mesure ->
                    try {
                        mesure.dateHeure.toLongOrNull() ?: 0L
                    } catch (e: Exception) {
                        0L
                    }
                }
                _historiqueState.postValue(HistoriqueState.Success(mesuresTriees))
            },
            onError = { error ->
                _historiqueState.postValue(HistoriqueState.Error(error))
            }
        )
    }
} 