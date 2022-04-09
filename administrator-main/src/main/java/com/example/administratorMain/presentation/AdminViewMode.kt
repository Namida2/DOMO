package com.example.administratorMain.presentation

import androidx.lifecycle.viewModelScope
import com.example.administratorMain.domatn.di.AdminDepsStore
import com.example.administratorMain.domatn.di.AdminDepsStore.onNetworkConnectionLostCallback
import com.example.core.domain.entities.tools.NetworkConnectionListener
import com.example.core.domain.interfaces.BaseActivityViewModel
import com.example.featureEmployees.domain.di.EmployeesAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponent
import com.example.featureSettings.domain.di.SettingsAppComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AdminViewMode : BaseActivityViewModel(null) {
    lateinit var employeesAppComponent: EmployeesAppComponent
    lateinit var profileAppComponent: ProfileAppComponent
    lateinit var settingsAppComponent: SettingsAppComponent

    init {
        listenPermissionChanges(viewModelScope)
        viewModelScope.launch {
            NetworkConnectionListener.networkConnectionChanges.collect {
                if (!it) onNetworkConnectionLostCallback?.onConnectionLost()
            }
        }
    }

    override fun onCleared() {
        AdminDepsStore.onCleared()
        super.onCleared()
    }
}
