package com.example.featureLogIn.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.core.domain.entities.tools.TaskWithPassword
import com.example.core.domain.entities.tools.constants.EmployeePosts
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.entities.tools.constants.Messages.emptyFieldMessage
import com.example.core.domain.entities.tools.extensions.isEmptyField
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState
import com.example.core.domain.useCases.LeaveAccountUseCase
import com.example.core.domain.useCases.ReadAdminPasswordUseCase
import com.example.core.presentation.adminPasswordDialog.AdminPasswordDialogCallbacks
import com.example.featureLogIn.domain.LogInUseCase

sealed class LogInVMStates {
    object Validating : LogInVMStates()
    class OnFailure(val errorMessage: ErrorMessage): LogInVMStates(), TerminatingState
    class Success(val employee: Employee) : LogInVMStates(), TerminatingState
    class RequestPassword(val correctPassword: String) : LogInVMStates()
    object Default : LogInVMStates()
}

class LogInViewModel(
    private val logInUseCase: LogInUseCase,
    private val readPasswordUseCase: ReadAdminPasswordUseCase,
    private val leaveAccountUseCase: LeaveAccountUseCase
) : ViewModel(), Stateful<LogInVMStates>, AdminPasswordDialogCallbacks{

    private var employee: Employee? = null
    private var _state = MutableLiveData<LogInVMStates>()
    val state: LiveData<LogInVMStates> = _state

    fun logIn(email: String, password: String) {
        setNewState(LogInVMStates.Validating)
        if (isEmptyField(email, password)) {
            setNewState(LogInVMStates.OnFailure(emptyFieldMessage))
            return
        }
        logInUseCase.logIn(email, password, object : TaskWithEmployee {
            override fun onSuccess(result: Employee) {
                employee = result
                if (result.post == EmployeePosts.ADMINISTRATOR.value) {
                    readAdminPassword()
                } else {
                    setNewState(LogInVMStates.Success(result))
                    return
                }
            }
            override fun onError(message: ErrorMessage?) {
                setNewState(LogInVMStates.OnFailure(message ?: defaultErrorMessage))
            }
        })
    }

    private fun readAdminPassword() {
        readPasswordUseCase.getPassword(object : TaskWithPassword {
            override fun onSuccess(result: String) {
                leaveAccountUseCase.leaveImmediately()
                setNewState(LogInVMStates.RequestPassword(result))
            }
            override fun onError(message: ErrorMessage?) {
                setNewState(LogInVMStates.OnFailure(message ?: defaultErrorMessage))
            }
        })
    }

    override fun setNewState(state: LogInVMStates) {
        _state.value = state
        if(state is TerminatingState)
            _state.value = LogInVMStates.Default
    }

    override fun onCorrectPassword() {
        setNewState(LogInVMStates.Validating)
        logInUseCase.logInAsAdministrator(employee!!, object : TaskWithEmployee {
            override fun onSuccess(result: Employee) {
                setNewState(LogInVMStates.Success(result))

            }
            override fun onError(message: ErrorMessage?) {
                setNewState(LogInVMStates.OnFailure(message ?: defaultErrorMessage)); return
            }
        })
    }

    override fun onDialogCanceled() { employee = null }

}