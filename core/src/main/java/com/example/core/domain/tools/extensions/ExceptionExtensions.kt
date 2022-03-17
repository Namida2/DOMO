package com.example.core.domain.tools.extensions

import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.constants.ErrorMessages.checkNetworkConnectionMessage
import com.example.core.domain.tools.constants.ErrorMessages.defaultErrorMessage
import com.example.core.domain.tools.constants.ErrorMessages.permissionDeniedMessage
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException

fun Exception.getExceptionMessage(): ErrorMessage = when (this) {
    is FirebaseNetworkException -> checkNetworkConnectionMessage
    is FirebaseFirestoreException -> this.code.getExceptionMessage()
    else -> defaultErrorMessage
}

fun FirebaseFirestoreException.Code.getExceptionMessage(): ErrorMessage =
    if (this == FirebaseFirestoreException.Code.PERMISSION_DENIED) permissionDeniedMessage
    else defaultErrorMessage