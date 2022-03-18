package com.example.featureEmployees.presentation.dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.Employee
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.SimpleTask
import com.example.core.domain.tools.constants.ErrorMessages.defaultErrorMessage
import com.example.featureEmployees.domain.useCases.DeleteEmployeeUseCase
import com.example.featureEmployees.domain.useCases.SetPermissionUseCase

sealed class EmployeeDetailVMStates {
    object Default : EmployeeDetailVMStates()
    object InProcess : EmployeeDetailVMStates()
    object OnSuccess : EmployeeDetailVMStates()
    class OnFailure(val errorMessage: ErrorMessage) : EmployeeDetailVMStates()
}

class EmployeeDetailViewModel(
    private val setPermissionUseCase: SetPermissionUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase
) : ViewModel() {

    private val _state = MutableLiveData<EmployeeDetailVMStates>()
    val state: LiveData<EmployeeDetailVMStates> = _state

    fun setPermission(employee: Employee, permission: Boolean) {
        _state.value = EmployeeDetailVMStates.InProcess
        setPermissionUseCase.setPermissionForEmployee(employee, permission, object : SimpleTask {
            override fun onSuccess(arg: Unit) {
                setTerminatingState(EmployeeDetailVMStates.OnSuccess)
            }

            override fun onError(message: ErrorMessage?) {
                setTerminatingState(
                    EmployeeDetailVMStates.OnFailure(message ?: defaultErrorMessage)
                )
            }
        })
    }

    fun deleteEmployee(employee: Employee) {
        _state.value = EmployeeDetailVMStates.InProcess
        deleteEmployeeUseCase.deleteEmployee(employee, object : SimpleTask {
            override fun onSuccess(arg: Unit) {
                setTerminatingState(EmployeeDetailVMStates.OnSuccess)
            }

            override fun onError(message: ErrorMessage?) {
                setTerminatingState(
                    EmployeeDetailVMStates.OnFailure(message ?: defaultErrorMessage)
                )
            }
        })
    }

    private fun setTerminatingState(state: EmployeeDetailVMStates) {
        _state.value = state
        _state.value = EmployeeDetailVMStates.Default
    }
}