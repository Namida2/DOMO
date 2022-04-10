package com.example.featureRegistration.presentation

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
import com.example.core.domain.entities.tools.constants.Messages.tooShortPasswordMessage
import com.example.core.domain.entities.tools.constants.Messages.wrongEmailOrPassword
import com.example.core.domain.entities.tools.constants.Messages.wrongPasswordConfirmationMessage
import com.example.core.domain.entities.tools.extensions.isEmptyField
import com.example.core.domain.entities.tools.extensions.isValidEmail
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState
import com.example.core.domain.useCases.LeaveAccountUseCase
import com.example.core.domain.useCases.ReadAdminPasswordUseCase
import com.example.core.presentation.adminPasswordDialog.AdminPasswordDialogCallbacks
import com.example.featureRegistration.domain.PostItem
import com.example.featureRegistration.domain.useCases.GetPostItemsUseCase
import com.example.featureRegistration.domain.useCases.RegistrationUseCase

sealed class RegistrationVMStates {
    object Default : RegistrationVMStates()
    object Validating : RegistrationVMStates()
    class RequestPassword(val correctPassword: String) : RegistrationVMStates()
    class OnFailure(val errorMessage: ErrorMessage) : RegistrationVMStates(), TerminatingState
    class Valid(val employee: Employee) : RegistrationVMStates(), TerminatingState
}

class RegistrationViewModel(
    private val getPostItemsUseCase: GetPostItemsUseCase,
    private val registrationUseCase: RegistrationUseCase,
    private val readPasswordUseCase: ReadAdminPasswordUseCase,
    private val leaveAccountUseCase: LeaveAccountUseCase
) : ViewModel(), Stateful<RegistrationVMStates>, AdminPasswordDialogCallbacks {

    private var employee: Employee? = null
    var selectedPost: String = EmployeePosts.COOK.value
    private val minPasswordLength = 6
    private var _state = MutableLiveData<RegistrationVMStates>()
    val state: LiveData<RegistrationVMStates> = _state

    fun validation(name: String, email: String, password: String, confirmPassword: String) {
        setNewState(RegistrationVMStates.Validating)
        when {
            isEmptyField(name, email, password, confirmPassword) -> {
                setNewState(RegistrationVMStates.OnFailure(emptyFieldMessage)); return
            }
            !email.isValidEmail() -> {
                setNewState(RegistrationVMStates.OnFailure(wrongEmailOrPassword)); return
            }
            password.length < minPasswordLength -> {
                setNewState(RegistrationVMStates.OnFailure(tooShortPasswordMessage)); return
            }
            password != confirmPassword -> {
                setNewState(RegistrationVMStates.OnFailure(wrongPasswordConfirmationMessage)); return
            }
        }
        register(Employee(email, name, selectedPost, password))
    }

    private fun register(employee: Employee) {
        registrationUseCase.registration(employee, object : TaskWithEmployee {
            override fun onSuccess(result: Employee) {
                this@RegistrationViewModel.employee = result
                if (result.post == EmployeePosts.ADMINISTRATOR.value) {
                    readAdminPassword()
                } else {
                    setNewState(RegistrationVMStates.Valid(result))
                    return
                }
            }
            override fun onError(message: ErrorMessage?) {
                setNewState(RegistrationVMStates.OnFailure(message ?: defaultErrorMessage)); return
            }
        })
    }

    private fun readAdminPassword() {
        readPasswordUseCase.getPassword(object : TaskWithPassword {
            override fun onSuccess(result: String) {
                leaveAccountUseCase.leaveImmediately()
                setNewState(RegistrationVMStates.RequestPassword(result))
            }
            override fun onError(message: ErrorMessage?) {
                setNewState(RegistrationVMStates.OnFailure(message ?: defaultErrorMessage))
            }
        })
    }

    fun getPostItems(): MutableList<PostItem> = getPostItemsUseCase.getPostItems()

    override fun setNewState(state: RegistrationVMStates) {
        _state.value = state
        if (state is TerminatingState)
            _state.value = RegistrationVMStates.Default
    }

    override fun onCorrectPassword() {
        setNewState(RegistrationVMStates.Validating)
        registrationUseCase.logInAsAdministrator(employee!!, object : TaskWithEmployee {
            override fun onSuccess(result: Employee) {
                setNewState(RegistrationVMStates.Valid(result))

            }
            override fun onError(message: ErrorMessage?) {
                setNewState(RegistrationVMStates.OnFailure(message ?: defaultErrorMessage)); return
            }
        })
    }

    override fun onDialogCanceled() { employee = null }

}
