package com.example.featureRegistration.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.core.domain.entities.tools.constants.EmployeePosts
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.entities.tools.constants.Messages.emailAlreadyExistsMessage
import com.example.core.domain.entities.tools.constants.Messages.emptyFieldMessage
import com.example.core.domain.entities.tools.constants.Messages.tooShortPasswordMessage
import com.example.core.domain.entities.tools.constants.Messages.wrongEmailOrPassword
import com.example.core.domain.entities.tools.constants.Messages.wrongPasswordConfirmationMessage
import com.example.core.domain.entities.tools.extensions.isEmptyField
import com.example.core.domain.entities.tools.extensions.isValidEmail
import com.example.featureRegistration.domain.useCases.GetPostItemsUseCase
import com.example.featureRegistration.R
import com.example.featureRegistration.domain.PostItem
import com.example.featureRegistration.domain.useCases.RegistrationUseCase

sealed class RegistrationViewModelStates {
    open var errorMessage: ErrorMessage? = null

    object Default : RegistrationViewModelStates()
    object Validating : RegistrationViewModelStates()
    object InvalidEmail : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? =
            wrongEmailOrPassword
    }

    object EmptyField : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = emptyFieldMessage
    }

    object ShortPassword : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? =
            tooShortPasswordMessage
    }

    object WrongPasswordConfirmation : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? =
            wrongPasswordConfirmationMessage
    }

    object EmailAlreadyExists : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? =
            emailAlreadyExistsMessage
    }

    object DefaultError : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = defaultErrorMessage
    }

    class Valid(val employee: Employee) : RegistrationViewModelStates()
}

class RegistrationViewModel(
    private val getPostItemsUseCase: GetPostItemsUseCase,
    private val registrationUseCase: RegistrationUseCase,
) : ViewModel() {

    var selectedPost: String = EmployeePosts.COOK.value
    private val minPasswordLength = 6
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
            password.length < minPasswordLength -> {
                _state.value = RegistrationViewModelStates.ShortPassword; return
            }
            password != confirmPassword -> {
                _state.value = RegistrationViewModelStates.WrongPasswordConfirmation; return
            }
        }

        val employee = Employee(email, name, selectedPost, password)
        registrationUseCase.registration(
            employee,
            object : TaskWithEmployee {
                override fun onSuccess(result: Employee) {
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
