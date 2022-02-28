package com.example.core.domain.tools

import com.example.core.R

object ErrorMessages {
    val networkConnectionMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.networkConnectionMessage
    )
    val defaultErrorMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.defaultMessage
    )
    val permissionErrorMessage = ErrorMessage(
        R.string.permissionErrorTitle,
        R.string.permissionErrorMessage
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