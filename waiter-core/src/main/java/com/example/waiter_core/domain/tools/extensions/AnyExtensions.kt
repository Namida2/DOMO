package com.example.waiter_core.domain.tools.extensions

import android.util.Log


fun Any.logD(message: String) {
    Log.d("MyLogging", message)
}

fun Any.logE(message: String) {
    Log.e("MyLogging", message)
}
