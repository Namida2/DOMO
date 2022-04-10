package com.example.core.domain.entities.tools.extensions

import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.entities.tools.constants.Messages.newMenuVersionMessage
import com.example.core.domain.entities.tools.constants.Messages.permissionDeniedMessage
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException

fun Exception.getExceptionMessage(): ErrorMessage = when (this) {
    is FirebaseNetworkException -> checkNetworkConnectionMessage
    is FirebaseFirestoreException -> this.code.getExceptionMessage()
    else -> defaultErrorMessage
}

fun FirebaseFirestoreException.Code.getExceptionMessage(): ErrorMessage =
    when {
        this == FirebaseFirestoreException.Code.PERMISSION_DENIED -> permissionDeniedMessage
        this == FirebaseFirestoreException.Code.UNAVAILABLE -> checkNetworkConnectionMessage
        else -> defaultErrorMessage
    }