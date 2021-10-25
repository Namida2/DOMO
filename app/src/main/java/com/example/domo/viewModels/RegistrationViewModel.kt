package com.example.domo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.R
import com.example.domo.models.RegistrationModel
import com.example.domo.views.PostItem
import constants.EmployeePosts
import entities.Employee
import tools.ErrorMessage

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
            R.string.wrongConfirmPasswordTitle,
            R.string.wrongConfirmPasswordMessage
        )
    }
    object EmailAlreadyExists : RegistrationViewModelStates() {
        override var errorMessage: ErrorMessage? = ErrorMessage(
            R.string.emailAlreadyExitTitle,
            R.string.emailAlreadyExistMessage
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

class RegistrationViewModel(private val model: RegistrationModel) : ViewModel() {

    var selectedPost: String = EmployeePosts.COOK
    private val MIN_PASSWORD_LENGH = 6
    private var _state =
        MutableLiveData<RegistrationViewModelStates>(RegistrationViewModelStates.Default)
    val state = _state

    fun validation(name: String, email: String, password: String, confirmPassword: String) {
        if (anyFieldIsEmpty(name, email, password, confirmPassword)) {
            state.value = RegistrationViewModelStates.EmptyField
            return
        }
        if (!isValidEmail(email)) {
            state.value = RegistrationViewModelStates.InvalidEmail
            return
        }
        if (password.length < MIN_PASSWORD_LENGH) {
            state.value = RegistrationViewModelStates.ShortPassword
            return
        }
        if (password != confirmPassword) {
            state.value = RegistrationViewModelStates.WrongPasswordConfirmation
            return
        }
        val employee: Employee = Employee(email, name, selectedPost, password)
        model.registration(employee, {
            state.value = RegistrationViewModelStates.Valid(employee)
        }, {
            when (it.titleId) {
                R.string.emailAlreadyExitTitle -> {
                    state.value = RegistrationViewModelStates.EmailAlreadyExists
                }
                R.string.defaultTitle -> {
                    state.value = RegistrationViewModelStates.DefaultError
                }
            }
        })
    }

    fun getPostItems(): MutableList<PostItem> = model.getPostItems()

    private fun anyFieldIsEmpty(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return name.isEmpty() || name.replace(" ", "").isEmpty() ||
                email.isEmpty() || email.replace(" ", "").isEmpty() ||
                password.isEmpty() || password.replace(" ", "").isEmpty() ||
                confirmPassword.isEmpty() || confirmPassword.replace(" ", "").isEmpty()
    }

    private fun isValidEmail(email: String) =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

}