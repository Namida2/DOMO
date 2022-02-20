package com.example.domo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.domo.R
import com.example.domo.models.interfaces.RegistrationModelInterface
import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.tools.constants.EmployeePosts
import entities.*
import com.example.waiterCore.domain.tools.TaskWithEmployee
import com.example.waiterCore.domain.tools.extensions.isEmptyField
import com.example.waiterCore.domain.tools.extensions.isValidEmail

sealed class RegistrationViewModelStates {
    open var errorMessage: ErrorMessage? = null

    object Default : RegistrationViewModelStates()
    object Validating : RegistrationViewModelStates()
    object InvalidEmail : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = ErrorMessage(
            R.string.wrongEmailFormatTitle,
            R.string.wrongEmailFormatMessage
        )
    }

    object EmptyField : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = ErrorMessage(
            R.string.emptyFieldTitle,
            R.string.emptyFieldMessage
        )
    }

    object ShortPassword : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = ErrorMessage(
            R.string.tooShortPasswordTitle,
            R.string.tooShortPasswordMessage
        )
    }

    object WrongPasswordConfirmation : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = ErrorMessage(
            R.string.wrongPasswordConfirmationTitle,
            R.string.wrongPasswordConfirmationMessage
        )
    }

    object EmailAlreadyExists : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = ErrorMessage(
            R.string.emailAlreadyExitsTitle,
            R.string.emailAlreadyExistsMessage
        )
    }

    object DefaultError : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = ErrorMessage(
            R.string.defaultTitle,
            R.string.defaultMessage
        )
    }

    class Valid(val employee: Employee) : RegistrationViewModelStates()
}

class RegistrationViewModel(private val model: RegistrationModelInterface) : ViewModel() {

    var selectedPost: String = EmployeePosts.COOK
    private val MIN_PASSWORD_LENGH = 6
    private var _state =
        MutableLiveData<RegistrationViewModelStates>(RegistrationViewModelStates.Default)
    val state = _state

    fun validation(name: String, email: String, password: String, confirmPassword: String) {
        state.value = RegistrationViewModelStates.Validating
        when {
            isEmptyField(name, email, password, confirmPassword) -> {
                state.value = RegistrationViewModelStates.EmptyField; return
            }
            !email.isValidEmail() -> {
                state.value = RegistrationViewModelStates.InvalidEmail; return
            }
            password.length < MIN_PASSWORD_LENGH -> {
                state.value = RegistrationViewModelStates.ShortPassword; return
            }
            password != confirmPassword -> {
                state.value = RegistrationViewModelStates.WrongPasswordConfirmation; return
            }
        }

        val employee = Employee(email, name, selectedPost, password)
        model.registration(
            employee,
            object : TaskWithEmployee {
                override fun onSuccess(arg: Employee) {
                    state.value = RegistrationViewModelStates.Valid(employee)
                }

                override fun onError(message: ErrorMessage?) {
                    when (message!!.titleId) {
                        R.string.emailAlreadyExitsTitle -> {
                            state.value = RegistrationViewModelStates.EmailAlreadyExists
                        }
                        R.string.defaultTitle -> {
                            state.value = RegistrationViewModelStates.DefaultError
                        }
                    }
                }
            }
        )
    }

    fun resetState() {
        state.value = RegistrationViewModelStates.Default
    }

    fun getPostItems(): MutableList<PostItem> = model.getPostItems()
}




