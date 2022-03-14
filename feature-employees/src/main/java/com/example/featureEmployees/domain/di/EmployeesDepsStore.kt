package com.example.featureEmployees.domain.di

object EmployeesDepsStore {
    val appComponent: EmployeesAppComponent by lazy {
        DaggerEmployeesAppComponent.builder().build()
    }
}
