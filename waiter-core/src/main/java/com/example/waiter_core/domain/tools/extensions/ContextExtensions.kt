package com.example.waiter_core.domain.tools.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.waiter_core.domain.tools.ErrorMessage
import com.example.waiter_core.domain.tools.dialogs.MessageAlertDialog

class ContextExtensions {

    fun Context.createMessageDialog(message: ErrorMessage): DialogFragment? =
        MessageAlertDialog.getNewInstance<Unit>(
            this.resources.getString(message.titleId),
            this.resources.getString(message.messageId)
        )

    fun Context.isNetworkConnected(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
        }
        return false
    }
}