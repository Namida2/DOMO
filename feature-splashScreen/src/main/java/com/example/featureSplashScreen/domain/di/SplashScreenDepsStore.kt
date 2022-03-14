package com.example.featureSplashScreen.domain.di

import com.example.core.domain.Employee
import com.example.core.domain.interfaces.EmployeeAuthCallback

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
