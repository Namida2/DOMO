package com.example.waiterMain.presentation

import androidx.lifecycle.viewModelScope
import com.example.core.domain.interfaces.BaseActivityViewModel
import com.example.core.domain.listeners.MenuVersionListener
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponent
import com.example.featureOrder.domain.di.OrderAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponent
import com.example.waiterMain.domain.di.WaiterMainDepsStore

class WaiterMainViewModel(
    menuVersionListener: MenuVersionListener
) : BaseActivityViewModel(menuVersionListener) {
    lateinit var profileAppComponent: ProfileAppComponent
    lateinit var orderAppComponents: OrderAppComponent
    lateinit var currentOrdersAppComponents: CurrentOrdersAppComponent
    init {
        listenPermissionChanges(viewModelScope)
        listenMenuVersionChanges(viewModelScope)
    }

    override fun onCleared() {
        WaiterMainDepsStore.onCleared()
        super.onCleared()
    }
}
