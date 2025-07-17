package bm.babimumba.diabete.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bm.babimumba.diabete.model.DonneeMedicale
import bm.babimumba.diabete.repository.UserRepository

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val derniereMesure: DonneeMedicale?) : HomeState()
    data class Error(val message: String) : HomeState()
}

class HomeViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {
    private val _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState> = _homeState

    private val _tendancesState = MutableLiveData<List<bm.babimumba.diabete.model.DonneeMedicale>>()
    val tendancesState: LiveData<List<bm.babimumba.diabete.model.DonneeMedicale>> = _tendancesState

    fun chargerDerniereMesure(patientId: String) {
        _homeState.value = HomeState.Loading
        userRepository.getDerniereDonneeMedicale(
            patientId,
            onSuccess = { derniereMesure ->
                _homeState.postValue(HomeState.Success(derniereMesure))
            },
            onError = { error ->
                _homeState.postValue(HomeState.Error(error))
            }
        )
    }

    fun chargerTendances(patientId: String) {
        userRepository.getDonneesTendances(
            patientId,
            onSuccess = { mesures ->
                _tendancesState.postValue(mesures)
            },
            onError = { error ->
                // Gérer l'erreur si nécessaire
            }
        )
    }

    fun calculerStatistiques(mesures: List<bm.babimumba.diabete.model.DonneeMedicale>): Triple<Double, Double, Double> {
        return userRepository.calculerStatistiques(mesures)
    }
} 