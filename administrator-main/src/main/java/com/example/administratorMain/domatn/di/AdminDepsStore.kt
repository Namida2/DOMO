package com.example.administratorMain.domatn.di

import com.example.core.domain.interfaces.EmployeeAuthCallback

object AdminDepsStore {
    lateinit var deps: AdminAppComponentDeps
    lateinit var employeeAuthCallback: EmployeeAuthCallback
    val appComponent: AdminAppComponent by lazy {
        DaggerAdminAppComponent.builder().adminAppComponentDeps(deps).build()
    }
}