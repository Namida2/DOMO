package com.example.featureSplashScreen.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.core.domain.entities.tools.constants.FirestoreReferences.actualMenuCollectionRef
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.entities.tools.constants.Messages.employeeDoesNotExists
import com.example.core.domain.entities.tools.constants.Messages.permissionDeniedMessage
import com.example.core.domain.useCases.ReadMenuUseCase
import com.example.featureSplashScreen.domain.useCases.GetCurrentEmployeeUseCase
import com.example.featureSplashScreen.domain.useCases.ReadOrdersUseCase
import com.example.featureSplashScreen.domain.useCases.ReadSettingsUseCase

sealed class SplashScreenStates {
    object DefaultState : SplashScreenStates()
    object ReadingData : SplashScreenStates()
    class EmployeeAndSettingsExist(
        var employee: Employee
    ) : SplashScreenStates()

    class OnFailure(
        var message: ErrorMessage
    ) : SplashScreenStates()

    object EmployeeDoesNotExists : SplashScreenStates()
}

class SplashScreenViewModel(
    readMenuUseCase: ReadMenuUseCase,
    readOrdersUseCase: ReadOrdersUseCase,
    private val settingsUseCase: ReadSettingsUseCase,
    private val getCurrentEmployeeUseCase: GetCurrentEmployeeUseCase
) : ViewModel() {
    private var currentEmployee: Employee? = null
    private var isSettingsExists = false
    private var _state: MutableLiveData<SplashScreenStates> =
        MutableLiveData(SplashScreenStates.DefaultState)
    val state: LiveData<SplashScreenStates> = _state

    init {
        readMenuUseCase.readMenu(actualMenuCollectionRef, false, object : SimpleTask {
            override fun onSuccess(result: Unit) {}
            override fun onError(message: ErrorMessage?) {}
        })
        readOrdersUseCase.readOrders()
    }

    fun getCurrentEmployee() {
        _state.value = SplashScreenStates.ReadingData
        getCurrentEmployeeUseCase.getCurrentEmployee(object : TaskWithEmployee {
            override fun onSuccess(result: Employee) {
                if (result.permission) {
                    currentEmployee = result
                    if (isSettingsExists)
                        _state.value = SplashScreenStates.EmployeeAndSettingsExist(result)
                } else _state.value = SplashScreenStates.OnFailure(permissionDeniedMessage)
            }

            override fun onError(message: ErrorMessage?) {
                if (message == employeeDoesNotExists)
                    _state.value = SplashScreenStates.EmployeeDoesNotExists
                else
                    _state.value = SplashScreenStates.OnFailure(message ?: defaultErrorMessage)
            }
        })
    }

    fun readSettings() {
        settingsUseCase.readSettings(object : SimpleTask {
            override fun onSuccess(result: Unit) {
                isSettingsExists = true
                if (currentEmployee != null)
                    _state.value =
                        SplashScreenStates.EmployeeAndSettingsExist(currentEmployee!!)
            }

            override fun onError(message: ErrorMessage?) {
                if (currentEmployee == null) return
                _state.value = SplashScreenStates.OnFailure(message ?: defaultErrorMessage)
            }
        })
    }

}