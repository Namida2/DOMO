package com.example.domo.viewModels.activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.Employee
import com.example.domo.models.interfaces.SplashScreenModelInterface
import kotlinx.coroutines.launch

sealed class SplashScreenStates {
    object DefaultState : SplashScreenStates()
    object CheckingForCurrentEmployee : SplashScreenStates()
    class EmployeeExists(var employee: Employee) : SplashScreenStates()
    object EmployeeDoesNotExit : SplashScreenStates()
}

class SplashScreenViewModel(
    private val model: SplashScreenModelInterface
) : ViewModel() {
    private var _state: MutableLiveData<SplashScreenStates> =
        MutableLiveData(SplashScreenStates.DefaultState)
    val state = _state

    init {
        model.readMenu()
    }

    fun getCurrentEmployee() {
        viewModelScope.launch {
            _state.value = SplashScreenStates.CheckingForCurrentEmployee
            model.getCurrentEmployee(object : com.example.core.domain.tools.Task<Employee, Unit> {
                override fun onSuccess(arg: Employee) {
                    state.value = SplashScreenStates.EmployeeExists(arg)
                }

                override fun onError(message: com.example.core.domain.tools.ErrorMessage?) {
                    state.value = SplashScreenStates.EmployeeDoesNotExit
                }
            })
        }
    }
}