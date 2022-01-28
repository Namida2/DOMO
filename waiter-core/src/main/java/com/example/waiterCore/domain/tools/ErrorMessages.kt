package com.example.waiterCore.domain.tools

import com.example.waiterCore.R

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
}