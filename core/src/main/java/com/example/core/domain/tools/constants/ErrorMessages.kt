package com.example.core.domain.tools.constants

import com.example.core.R
import com.example.core.domain.tools.ErrorMessage

object ErrorMessages {
    val checkNetworkConnectionMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.networkConnectionMessage
    )
    val defaultErrorMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.defaultMessage
    )
    val employeeDoesNotExists = ErrorMessage(
        R.string.defaultTitle,
        R.string.employeeDoesNotExists
    )
    val permissionDeniedMessage = ErrorMessage(
        R.string.permissionDeniedTitle,
        R.string.permissionDeniedMessage
    )
    val wrongEmailOrPassword = ErrorMessage(
        R.string.wrongEmailOrPasswordTitle,
        R.string.wrongEmailOrPasswordMessage
    )
    val emptyFieldMessage: ErrorMessage = ErrorMessage(
        R.string.emptyFieldTitle,
        R.string.emptyFieldMessage
    )

    val tooShortPasswordMessage: ErrorMessage = ErrorMessage(
        R.string.tooShortPasswordTitle,
        R.string.tooShortPasswordMessage
    )

    val emailAlreadyExistsMessage: ErrorMessage? = ErrorMessage(
        R.string.emailAlreadyExitsTitle,
        R.string.emailAlreadyExistsMessage
    )

    val wrongPasswordConfirmationMessage: ErrorMessage? = ErrorMessage(
        R.string.wrongPasswordConfirmationTitle,
        R.string.wrongPasswordConfirmationMessage
    )
}