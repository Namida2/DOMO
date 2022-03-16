package com.example.featureEmployees.domain.di

object EmployeesDepsStore {
    lateinit var deps: EmployeesAppComponentDeps
    val appComponent: EmployeesAppComponent by lazy {
        DaggerEmployeesAppComponent.builder().employeesAppComponentDeps(deps).build()
    }
}
