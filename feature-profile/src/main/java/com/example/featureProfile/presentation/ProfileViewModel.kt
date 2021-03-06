package com.example.featureProfile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.useCases.LeaveAccountUseCase
import com.example.featureProfile.domain.di.ProfileDepsStore.deps

sealed class ProfileViewModelStates {
    object Default : ProfileViewModelStates()
    object LogOutWasSuccessful : ProfileViewModelStates()
    object TryingToLogOut : ProfileViewModelStates()
    class LogOutFailed(val errorMessage: ErrorMessage) : ProfileViewModelStates()
}

class ProfileViewModel(
    private val leaveAccountUseCase: LeaveAccountUseCase
) : ViewModel() {

    private val _state = MutableLiveData<ProfileViewModelStates>(ProfileViewModelStates.Default)
    val state: LiveData<ProfileViewModelStates> = _state

    fun leaveThisAccount() {
        _state.value = ProfileViewModelStates.TryingToLogOut
        leaveAccountUseCase.leaveThisAccount(deps.currentEmployee!!, object : SimpleTask {
            override fun onSuccess(result: Unit) {
                _state.value = ProfileViewModelStates.LogOutWasSuccessful
            }

            override fun onError(message: ErrorMessage?) {
                _state.value = ProfileViewModelStates.LogOutFailed(message ?: defaultErrorMessage)
            }
        })
    }
}