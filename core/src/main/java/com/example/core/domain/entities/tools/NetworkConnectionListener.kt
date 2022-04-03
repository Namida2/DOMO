package com.example.core.domain.entities.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.flow.MutableSharedFlow

object NetworkConnectionListener {
    private lateinit var connectivityManager: ConnectivityManager
    val networkConnectionChanges = MutableSharedFlow<Boolean>(replay = 1)
    fun registerCallback(context: Context) {
        if(this::connectivityManager.isInitialized) return
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                networkConnectionChanges.tryEmit(true)
            }
            override fun onLost(network: Network) {
                networkConnectionChanges.tryEmit(false)
            }
        })
    }

}