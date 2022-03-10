package com.example.featureProfile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.ErrorMessages.defaultErrorMessage
import com.example.core.domain.tools.SimpleTask
import com.example.featureProfile.domain.LeaveAccountUseCase

sealed class ProfileViewModelStates {
    object Default : ProfileViewModelStates()
    object LogOutWasSuccessful : ProfileViewModelStates(), TerminatedViewModelState
    object TryingToLogOut : ProfileViewModelStates()
    class LogOutFailed(val errorMessage: ErrorMessage) : ProfileViewModelStates()
}
interface TerminatedViewModelState

class ProfileViewModel(
    private val leaveAccountUseCase: LeaveAccountUseCase
) : ViewModel() {


    private val _state = MutableLiveData<ProfileViewModelStates>(ProfileViewModelStates.Default)
    val state: LiveData<ProfileViewModelStates> = _state

    fun leaveThisAccount() {
        _state.value = ProfileViewModelStates.TryingToLogOut
        leaveAccountUseCase.leaveThisAccount(object : SimpleTask {
            override fun onSuccess(arg: Unit) {
                _state.value = ProfileViewModelStates.LogOutWasSuccessful
            }

            override fun onError(message: ErrorMessage?) {
                _state.value = ProfileViewModelStates.LogOutFailed(message ?: defaultErrorMessage)
            }
        })
    }
}