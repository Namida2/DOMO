package com.example.domo.splashScreen.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.Employee
import com.example.core.domain.tools.ErrorMessages.permissionErrorMessage
import com.example.domo.splashScreen.domain.GetCurrentEmployeeUseCase
import com.example.domo.splashScreen.domain.ReadMenuUseCase
import kotlinx.coroutines.launch

sealed class SplashScreenStates {
    object DefaultState : SplashScreenStates()
    object CheckingForCurrentEmployee : SplashScreenStates()
    class EmployeeExists(
        var employee: Employee
    ) : SplashScreenStates()

    class PermissionError(
        var message: com.example.core.domain.tools.ErrorMessage = permissionErrorMessage
    ) : SplashScreenStates()

    object EmployeeDoesNotExit : SplashScreenStates()
}

class SplashScreenViewModel(
    readMenuUseCase: ReadMenuUseCase,
    readOrdersUseCase: ReadOrdersUseCase,
    private val getCurrentEmployeeUseCase: GetCurrentEmployeeUseCase
) : ViewModel() {
    private var _state: MutableLiveData<SplashScreenStates> =
        MutableLiveData(SplashScreenStates.DefaultState)
    val state: LiveData<SplashScreenStates> = _state

    init {
        //TODO: Read the menu if it connected to the network
        readMenuUseCase.readMenu()
        readOrdersUseCase.readOrders()
    }

    fun getCurrentEmployee() {
        viewModelScope.launch {
            _state.value = SplashScreenStates.CheckingForCurrentEmployee
            getCurrentEmployeeUseCase.getCurrentEmployee(object :
                com.example.core.domain.tools.Task<Employee, Unit> {
                override fun onSuccess(emplpoyee: Employee) {
                    if (emplpoyee.permission)
                        _state.value = SplashScreenStates.EmployeeExists(emplpoyee)
                    else _state.value = SplashScreenStates.PermissionError()
                }

                override fun onError(message: com.example.core.domain.tools.ErrorMessage?) {
                    _state.value = SplashScreenStates.EmployeeDoesNotExit
                }
            })
        }
    }

}