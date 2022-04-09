package com.example.waiterMain.presentation

import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.tools.NetworkConnectionListener
import com.example.core.domain.interfaces.BaseActivityViewModel
import com.example.core.domain.listeners.MenuVersionListener
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponent
import com.example.featureOrder.domain.di.OrderAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponent
import com.example.waiterMain.domain.di.WaiterMainDepsStore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WaiterMainViewModel(
    menuVersionListener: MenuVersionListener
) : BaseActivityViewModel(menuVersionListener) {
    lateinit var profileAppComponent: ProfileAppComponent
    lateinit var orderAppComponents: OrderAppComponent
    lateinit var currentOrdersAppComponents: CurrentOrdersAppComponent

    init {
        listenPermissionChanges(viewModelScope)
        listenMenuVersionChanges(viewModelScope)
        viewModelScope.launch {
            NetworkConnectionListener.networkConnectionChanges.collect {
                if(!it) WaiterMainDepsStore.onNetworkConnectionLostCallback?.onConnectionLost()
            }
        }
    }

    override fun onCleared() {
        WaiterMainDepsStore.onCleared()
        super.onCleared()
    }
}
