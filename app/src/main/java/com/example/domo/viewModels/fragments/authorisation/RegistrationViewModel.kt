package com.example.domo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.Employee
import com.example.core.domain.tools.extensions.isValidEmail
import com.example.domo.R
import com.example.domo.models.interfaces.RegistrationModelInterface
import com.example.featureRegistration.domain.PostItem

sealed class RegistrationViewModelStates {
    open var errorMessage: com.example.core.domain.tools.ErrorMessage? = null

    object Default : RegistrationViewModelStates()
    object Validating : RegistrationViewModelStates()
    object InvalidEmail : RegistrationViewModelStates() {
        override var errorMessage: com.example.core.domain.tools.ErrorMessage? =
            com.example.core.domain.tools.ErrorMessage(
                R.string.wrongEmailFormatTitle,
                R.string.wrongEmailFormatMessage
            )
    }

    object EmptyField : RegistrationViewModelStates() {
        override var errorMessage: com.example.core.domain.tools.ErrorMessage? =
            com.example.core.domain.tools.ErrorMessage(
                R.string.emptyFieldTitle,
                R.string.emptyFieldMessage
            )
    }

    object ShortPassword : RegistrationViewModelStates() {
        override var errorMessage: com.example.core.domain.tools.ErrorMessage? =
            com.example.core.domain.tools.ErrorMessage(
                R.string.tooShortPasswordTitle,
                R.string.tooShortPasswordMessage
            )
    }

    object WrongPasswordConfirmation : RegistrationViewModelStates() {
        override var errorMessage: com.example.core.domain.tools.ErrorMessage? =
            com.example.core.domain.tools.ErrorMessage(
                R.string.wrongPasswordConfirmationTitle,
                R.string.wrongPasswordConfirmationMessage
            )
    }

    object EmailAlreadyExists : RegistrationViewModelStates() {
        override var errorMessage: com.example.core.domain.tools.ErrorMessage? =
            com.example.core.domain.tools.ErrorMessage(
                R.string.emailAlreadyExitsTitle,
                R.string.emailAlreadyExistsMessage
            )
    }

    object DefaultError : RegistrationViewModelStates() {
        override var errorMessage: com.example.core.domain.tools.ErrorMessage? =
            com.example.core.domain.tools.ErrorMessage(
                R.string.defaultTitle,
                R.string.defaultMessage
            )
    }

    class Valid(val employee: Employee) : RegistrationViewModelStates()
}

class RegistrationViewModel(private val model: RegistrationModelInterface) : ViewModel() {

    var selectedPost: String = com.example.core.domain.tools.constants.EmployeePosts.COOK
    private val MIN_PASSWORD_LENGH = 6
    private var _state =
        MutableLiveData<RegistrationViewModelStates>(RegistrationViewModelStates.Default)
    val state = _state

    fun validation(name: String, email: String, password: String, confirmPassword: String) {
        state.value = RegistrationViewModelStates.Validating
        when {
            com.example.core.domain.tools.extensions.isEmptyField(
                name,
                email,
                password,
                confirmPassword
            ) -> {
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
            object : com.example.core.domain.tools.TaskWithEmployee {
                override fun onSuccess(arg: Employee) {
                    state.value = RegistrationViewModelStates.Valid(employee)
                }

                override fun onError(message: com.example.core.domain.tools.ErrorMessage?) {
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




