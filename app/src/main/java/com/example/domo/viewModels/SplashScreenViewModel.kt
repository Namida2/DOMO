package com.example.domo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domo.models.SplashScreenModel
import entities.Employee
import entities.Task
import kotlinx.coroutines.launch

sealed class SplashScreenStates {
    object DefaultState : SplashScreenStates()
    object CheckingForCurrentEmployee : SplashScreenStates()
    class EmployeeExists(var employee: Employee) : SplashScreenStates()
    object EmployeeDoesNotExit : SplashScreenStates()
}

class SplashScreenViewModel(
    private val model: SplashScreenModel
) : ViewModel() {
    private var _state: MutableLiveData<SplashScreenStates> =
        MutableLiveData(SplashScreenStates.DefaultState)
    val state = _state

    fun getCurrentEmployee() {
        viewModelScope.launch {
            _state.value = SplashScreenStates.CheckingForCurrentEmployee
            model.getCurrentEmployee(object : Task<Employee, Unit, Unit> {
                override fun onSuccess(arg: Employee) {
                    state.value = SplashScreenStates.EmployeeExists(arg)
                }
                override fun onError(arg: Unit) {
                    state.value = SplashScreenStates.EmployeeDoesNotExit
                }
            })
        }
    }
}