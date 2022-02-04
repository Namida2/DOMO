package com.example.domo.registration.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.R
import com.example.domo.registration.domain.GetPostItemsUseCase
import com.example.domo.registration.domain.RegistrationUseCase
import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.ErrorMessages.defaultErrorMessage
import com.example.waiterCore.domain.tools.ErrorMessages.emailAlreadyExistsMessage
import com.example.waiterCore.domain.tools.ErrorMessages.emptyFieldMessage
import com.example.waiterCore.domain.tools.ErrorMessages.tooShortPasswordMessage
import com.example.waiterCore.domain.tools.ErrorMessages.wrongEmailOrPassword
import com.example.waiterCore.domain.tools.ErrorMessages.wrongPasswordConfirmationMessage
import com.example.waiterCore.domain.tools.TaskWithEmployee
import com.example.waiterCore.domain.tools.extensions.isEmptyField
import com.example.waiterCore.domain.tools.extensions.isValidEmail
import entities.PostItem
import entities.constants.EmployeePosts

sealed class RegistrationViewModelStates {
    open var errorMessage: ErrorMessage? = null

    object Default : RegistrationViewModelStates()
    object Validating : RegistrationViewModelStates()
    object InvalidEmail : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = wrongEmailOrPassword
    }

    object EmptyField : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = emptyFieldMessage
    }

    object ShortPassword : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = tooShortPasswordMessage
    }

    object WrongPasswordConfirmation : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = wrongPasswordConfirmationMessage
    }

    object EmailAlreadyExists : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = emailAlreadyExistsMessage
    }

    object DefaultError : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = defaultErrorMessage
    }

    class Valid(val employee: Employee) : RegistrationViewModelStates()
}

class RegistrationViewModel(
    private val getPostItemsUseCase: GetPostItemsUseCase,
    private val registrationUseCaseImpl: RegistrationUseCase,
) : ViewModel() {

    var selectedPost: String = EmployeePosts.COOK
    private val MIN_PASSWORD_LENGH = 6
    private var _state =
        MutableLiveData<RegistrationViewModelStates>(RegistrationViewModelStates.Default)
    val state: LiveData<RegistrationViewModelStates> = _state

    fun validation(name: String, email: String, password: String, confirmPassword: String) {
        _state.value = RegistrationViewModelStates.Validating
        when {
            isEmptyField(name, email, password, confirmPassword) -> {
                _state.value = RegistrationViewModelStates.EmptyField; return
            }
            !email.isValidEmail() -> {
                _state.value = RegistrationViewModelStates.InvalidEmail; return
            }
            password.length < MIN_PASSWORD_LENGH -> {
                _state.value = RegistrationViewModelStates.ShortPassword; return
            }
            password != confirmPassword -> {
                _state.value = RegistrationViewModelStates.WrongPasswordConfirmation; return
            }
        }

        val employee = Employee(email, name, selectedPost, password)
        registrationUseCaseImpl.registration(
            employee,
            object : TaskWithEmployee {
                override fun onSuccess(arg: Employee) {
                    _state.value = RegistrationViewModelStates.Valid(employee)
                }

                override fun onError(message: ErrorMessage?) {
                    when (message!!.titleId) {
                        R.string.emailAlreadyExitsTitle -> {
                            _state.value = RegistrationViewModelStates.EmailAlreadyExists
                        }
                        R.string.defaultTitle -> {
                            _state.value = RegistrationViewModelStates.DefaultError
                        }
                    }
                }
            }
        )
    }

    fun resetState() {
        _state.value = RegistrationViewModelStates.Default
    }

    fun getPostItems(): MutableList<PostItem> = getPostItemsUseCase.getPostItems()
}