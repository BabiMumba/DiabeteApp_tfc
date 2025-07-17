package bm.babimumba.diabete.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bm.babimumba.diabete.model.DonneeMedicale
import bm.babimumba.diabete.repository.UserRepository

sealed class AddMesureState {
    object Loading : AddMesureState()
    object Success : AddMesureState()
    data class Error(val message: String) : AddMesureState()
}

class AddMesureViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {
    private val _addMesureState = MutableLiveData<AddMesureState>()
    val addMesureState: LiveData<AddMesureState> = _addMesureState

    fun ajouterDonneeMedicale(donnee: DonneeMedicale) {
        _addMesureState.value = AddMesureState.Loading
        userRepository.ajouterDonneeMedicale(
            donnee,
            onSuccess = {
                _addMesureState.postValue(AddMesureState.Success)
            },
            onError = { error ->
                _addMesureState.postValue(AddMesureState.Error(error))
            }
        )
    }
} 