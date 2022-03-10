package com.example.core.domain.tools.extensions

import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.ErrorMessages
import com.google.firebase.FirebaseNetworkException

fun Exception.getExceptionMessage(): ErrorMessage =
    if (this is FirebaseNetworkException) ErrorMessages.checkNetworkConnectionMessage
    else if(this is PermissionEx)
    else ErrorMessages.defaultErrorMessage