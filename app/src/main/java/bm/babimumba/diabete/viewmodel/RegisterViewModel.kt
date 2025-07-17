package bm.babimumba.diabete.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bm.babimumba.diabete.model.Patient
import bm.babimumba.diabete.repository.UserRepository

sealed class RegisterState {
    object Loading : RegisterState()
    data class Success(val patient: Patient) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {
    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> = _registerState

    fun registerPatient(
        name: String,
        postnom: String,
        email: String,
        poids: String,
        dateNaissance: String,
        sexe: String,
        password: String,
        role: String
    ) {
        _registerState.value = RegisterState.Loading
        userRepository.registerPatient(
            name, postnom, email, poids, dateNaissance, sexe, password, role,
            onSuccess = { patient ->
                _registerState.postValue(RegisterState.Success(patient))
            },
            onError = { error ->
                _registerState.postValue(RegisterState.Error(error))
            }
        )
    }
} 