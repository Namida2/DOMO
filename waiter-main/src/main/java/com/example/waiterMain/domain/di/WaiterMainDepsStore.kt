package com.example.waiterMain.domain.di

import com.example.core.domain.Settings
import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponentDeps
import com.example.featureOrder.domain.di.OrderAppComponentDeps
import com.example.featureOrder.domain.di.OrderDepsStore
import com.example.featureProfile.domain.di.ProfileAppComponentDeps
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.google.firebase.auth.FirebaseAuth

object WaiterMainDepsStore {
    lateinit var deps: WaiterMainDeps
    lateinit var profileDeps: ProfileModuleDeps
    lateinit var employeeAuthCallback: EmployeeAuthCallback
    val appComponent: WaiterMainAppComponent by lazy {
        DaggerWaiterMainAppComponent.builder()
            .provideWaiterMainDeps(deps)
            .build()
    }
}

interface ProfileModuleDeps {
    val firebaseAuth: FirebaseAuth
}
