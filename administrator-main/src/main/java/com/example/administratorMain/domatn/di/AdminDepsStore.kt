package com.example.administratorMain.domatn.di

import com.example.core.domain.interfaces.BaseDepsStore
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.core.domain.interfaces.NewMenuVersionCallback
import com.example.core.domain.interfaces.OnNetworkConnectionLostCallback

object AdminDepsStore: BaseDepsStore {
    var deps: AdminAppComponentDeps? = null
    var employeeAuthCallback: EmployeeAuthCallback? = null
    var newMenuVersionCallback: NewMenuVersionCallback? = null
    var onNetworkConnectionLostCallback: OnNetworkConnectionLostCallback? = null

    override fun onCleared() {
        deps = null
        employeeAuthCallback = null
        newMenuVersionCallback = null
        onNetworkConnectionLostCallback = null
    }
}