package com.example.cookMain.presentation

import androidx.lifecycle.viewModelScope
import com.example.cookMain.domain.di.CookMainDepsStore
import com.example.core.domain.interfaces.BaseActivityViewModel
import com.example.core.domain.listeners.MenuVersionListener
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponent

class CookMainViewModel(
    menuVersionListener: MenuVersionListener
) : BaseActivityViewModel(menuVersionListener) {
    lateinit var currentOrdersAppComponents: CurrentOrdersAppComponent
    lateinit var profileAppComponent: ProfileAppComponent
    init {
        listenPermissionChanges(viewModelScope)
        listenMenuVersionChanges(viewModelScope)
    }
    override fun onCleared() {
        CookMainDepsStore.onCleared()
        super.onCleared()
    }
}