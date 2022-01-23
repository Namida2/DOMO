package com.example.waiter_core.domain.tools

import com.example.waiter_core.R

object ErrorMessages {
    val networkConnectionMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.networkConnectionMessage
    )
    val somethingWrongMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.defaultMessage
    )
    val permissionErrorMessage = ErrorMessage(
        R.string.permissionErrorTitle,
        R.string.permissionErrorMessage
    )
}