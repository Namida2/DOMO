package com.example.core.domain.entities.tools.constants

import com.example.core.R
import com.example.core.domain.entities.tools.ErrorMessage

object Messages {
    var dishNotFoundMessage: ErrorMessage =
        ErrorMessage(
            R.string.dishNotFoundTitle,
            R.string.dishNotFoundMessage
        )
    var dishAlreadyAddedMessage: ErrorMessage =
        ErrorMessage(
            R.string.dishAlreadyAddedTitle,
            R.string.dishAlreadyAddedMessage
        )
    val newMenuVersionMessage = ErrorMessage(
        R.string.newMenuVersionTitle,
        R.string.newMenuVersionMessage
    )
    val checkNetworkConnectionMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.networkConnectionMessage
    )
    val categoryAlreadyExists = ErrorMessage(
        R.string.defaultTitle,
        R.string.categoryAlreadyExistsMessage
    )
    val dishAlreadyExists = ErrorMessage(
        R.string.defaultTitle,
        R.string.gishAlreadyExistsMessage
    )
    val ordersCollectionNotEmptyMessage = ErrorMessage(
        R.string.ordersCollectionNotEmptyTitle,
        R.string.ordersCollectionNotEmptyMessage
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