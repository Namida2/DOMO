package com.example.administratorMain.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.administratorMain.domatn.di.AdminDepsStore
import com.example.core.domain.interfaces.BaseActivityViewModel
import com.example.featureEmployees.domain.di.EmployeesAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponent
import com.example.featureSettings.domain.di.SettingsAppComponent

class AdminViewMode : ViewModel() {
    lateinit var employeesAppComponent: EmployeesAppComponent
    lateinit var profileAppComponent: ProfileAppComponent
    lateinit var settingsAppComponent: SettingsAppComponent

    override fun onCleared() {
        AdminDepsStore.onCleared()
        super.onCleared()
    }
}
