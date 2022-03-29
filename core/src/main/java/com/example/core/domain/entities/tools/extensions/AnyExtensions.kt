package com.example.core.domain.entities.tools.extensions

import android.util.Log


fun Any.logD(message: String) {
    Log.d("MyLogging", message)
}

fun Any.logE(message: String) {
    Log.e("MyLogging", message)
}
