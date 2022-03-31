package com.example.administratorMain.domatn.di

import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.core.domain.interfaces.NewMenuVersionCallback

object AdminDepsStore {
    lateinit var deps: AdminAppComponentDeps
    lateinit var employeeAuthCallback: EmployeeAuthCallback
    lateinit var newMenuVersionCallback: NewMenuVersionCallback
}