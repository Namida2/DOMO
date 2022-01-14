package com.example.feature_splashscreen.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_splashscreen.domain.ReadMenuUseCase
import com.example.waiter_core.domain.Employee
import com.example.waiter_core.domain.tools.ErrorMessage
import com.example.waiter_core.domain.tools.Task
import kotlinx.coroutines.launch

sealed class SplashScreenStates {
    object DefaultState : SplashScreenStates()
    object CheckingForCurrentEmployee : SplashScreenStates()
    class EmployeeExists(var employee: Employee) : SplashScreenStates()
    object EmployeeDoesNotExit : SplashScreenStates()
}

//TODO: Add the useCase // STOPPED_1 //
class SplashScreenViewModel(
    private val model: ReadMenuUseCase
) : ViewModel() {
    private var _state: MutableLiveData<SplashScreenStates> =
        MutableLiveData(SplashScreenStates.DefaultState)
    val state = _state

    init {
        model.readMenu()
    }

    fun getCurrentEmployee() {
//        viewModelScope.launch {
//            _state.value = SplashScreenStates.CheckingForCurrentEmployee
//            model.getCurrentEmployee(object : Task<Employee, Unit> {
//                override fun onSuccess(arg: Employee) {
//                    state.value = SplashScreenStates.EmployeeExists(arg)
//                }
//
//                override fun onError(message: ErrorMessage?) {
//                    state.value = SplashScreenStates.EmployeeDoesNotExit
//                }
//            })
//        }
    }
}