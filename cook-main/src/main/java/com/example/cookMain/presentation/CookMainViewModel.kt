package com.example.cookMain.presentation

import androidx.lifecycle.viewModelScope
import com.example.cookMain.domain.di.CookMainDepsStore
import com.example.core.domain.entities.tools.NetworkConnectionListener
import com.example.core.domain.interfaces.BaseActivityViewModel
import com.example.core.domain.listeners.MenuVersionListener
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CookMainViewModel(
    menuVersionListener: MenuVersionListener
) : BaseActivityViewModel(menuVersionListener) {
    lateinit var currentOrdersAppComponents: CurrentOrdersAppComponent
    lateinit var profileAppComponent: ProfileAppComponent
    init {
        listenPermissionChanges(viewModelScope)
        listenMenuVersionChanges(viewModelScope)
        viewModelScope.launch {
            NetworkConnectionListener.networkConnectionChanges.collect {
                if(!it) CookMainDepsStore.onNetworkConnectionLostCallback?.onConnectionLost()
            }
        }
    }
    override fun onCleared() {
        CookMainDepsStore.onCleared()
        super.onCleared()
    }
}