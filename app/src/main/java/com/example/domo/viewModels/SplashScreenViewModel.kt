package com.example.domo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domo.modeles.SplashScreenModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashScreenStates {
    object DefaultState: SplashScreenStates()
    object CheckingForCurrentEmployee : SplashScreenStates()
    class UserExist(post: String) : SplashScreenStates()
    object UserDoesNotExit: SplashScreenStates()
}


class SplashScreenViewModel(
    private val splashScreenModel: SplashScreenModel
): ViewModel() {
    private val _state: MutableLiveData<SplashScreenStates> by lazy {
        MutableLiveData(SplashScreenStates.DefaultState)
    }
    val state = _state

    fun getCurrentUser() {
        viewModelScope.launch {
            splashScreenModel.getCurrentUser()
        }
    }

}