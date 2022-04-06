package com.example.administratorMain.domatn.di

import com.example.core.domain.interfaces.BaseDepsStore
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.core.domain.interfaces.NewMenuVersionCallback

object AdminDepsStore: BaseDepsStore {
    var deps: AdminAppComponentDeps? = null
    var employeeAuthCallback: EmployeeAuthCallback? = null
    var newMenuVersionCallback: NewMenuVersionCallback? = null

    override fun onCleared() {
        deps = null
        employeeAuthCallback = null
        newMenuVersionCallback = null
    }
}