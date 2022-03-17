package com.example.featureSplashScreen.domain.di

import com.example.core.data.listeners.EmployeePermissionListener
import com.example.core.domain.Employee
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.core.domain.tools.constants.ErrorMessages.permissionDeniedMessage
import com.example.core.domain.tools.extensions.createMessageDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

object SplashScreenDepsStore {
    val currentEmployee: Employee = Employee()
    fun setNewEmployeeData(newEmployee: Employee) {
        currentEmployee.email = newEmployee.email
        currentEmployee.name = newEmployee.name
        currentEmployee.post = newEmployee.post
        currentEmployee.password = newEmployee.password
        currentEmployee.permission = newEmployee.permission
    }
    lateinit var appComponent: SplashScreenAppComponent
}
