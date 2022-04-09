package com.example.cookMain.domain.di

import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.core.domain.interfaces.NewMenuVersionCallback
import com.example.core.domain.interfaces.OnNetworkConnectionLostCallback
import com.example.core.domain.interfaces.OrdersService
import com.google.firebase.auth.FirebaseAuth

object CookMainDepsStore {
    var deps: CookMainDeps? = null
    var employeeAuthCallback: EmployeeAuthCallback? = null
    var newMenuVersionCallback: NewMenuVersionCallback? = null
    var onNetworkConnectionLostCallback: OnNetworkConnectionLostCallback? = null
    var appComponent: CookMainAppComponent? = null
        get() = if (field == null) {
            field = DaggerCookMainAppComponent.builder().build(); field
        } else field

    fun onCleared() {
        deps = null
        employeeAuthCallback = null
        newMenuVersionCallback = null
        appComponent = null
        onNetworkConnectionLostCallback = null
    }
}

interface CookMainDeps {
    val currentEmployee: Employee?
    val firebaseAuth: FirebaseAuth
    val ordersService: OrdersService
}