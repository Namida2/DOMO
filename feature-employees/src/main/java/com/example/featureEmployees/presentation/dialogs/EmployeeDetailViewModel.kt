package com.example.featureEmployees.presentation.dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState
import com.example.featureEmployees.domain.useCases.DeleteEmployeeUseCase
import com.example.featureEmployees.domain.useCases.SetPermissionUseCase

sealed class EmployeeDetailVMStates {
    object Default : EmployeeDetailVMStates()
    object InProcess : EmployeeDetailVMStates()
    object OnSuccess : EmployeeDetailVMStates(), TerminatingState
    class OnFailure(val errorMessage: ErrorMessage) : EmployeeDetailVMStates(), TerminatingState
}

class EmployeeDetailViewModel(
    private val setPermissionUseCase: SetPermissionUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase
) : ViewModel(), Stateful<EmployeeDetailVMStates> {

    private val _state = MutableLiveData<EmployeeDetailVMStates>()
    val state: LiveData<EmployeeDetailVMStates> = _state

    private val simpleTask = object : SimpleTask {
        override fun onSuccess(result: Unit) {
            setNewState(EmployeeDetailVMStates.OnSuccess)
        }
        override fun onError(message: ErrorMessage?) {
            setNewState(EmployeeDetailVMStates.OnFailure(message ?: defaultErrorMessage))
        }
    }

    fun setPermission(employee: Employee, permission: Boolean) {
        setNewState(EmployeeDetailVMStates.InProcess)
        setPermissionUseCase.setPermissionForEmployee(employee, permission, simpleTask)
    }

    fun deleteEmployee(employee: Employee) {
        _state.value = EmployeeDetailVMStates.InProcess
        deleteEmployeeUseCase.deleteEmployee(employee, simpleTask)
    }

    override fun setNewState(state: EmployeeDetailVMStates) {
        _state.value = state
        if(state is TerminatingState)
            _state.value = EmployeeDetailVMStates.Default
    }
}