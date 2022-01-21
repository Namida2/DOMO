package extentions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.fragment.app.DialogFragment
import application.MyApplication
import di.AppComponent
import com.example.waiter_core.domain.Employee
import com.example.waiter_core.domain.tools.ErrorMessage
import com.example.waiter_core.domain.tools.dialogs.MessageAlertDialog

val Context.appComponent: AppComponent
    get() = when (this) {
        is MyApplication -> _appComponent
        else -> this.applicationContext.appComponent
    }
var Context.employee: Employee?
    get() = when (this) {
        is MyApplication -> _employee
        else -> this.applicationContext.employee
    }
    set(value) = when (this) {
        is MyApplication -> _employee = value
        else -> this.applicationContext.employee = value
    }


fun Context.createMessageDialog(message: ErrorMessage, action: (() -> Unit)? = null): DialogFragment? =
    MessageAlertDialog.getNewInstance<Unit>(
        this.resources.getString(message.titleId),
        this.resources.getString(message.messageId),
        action
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
