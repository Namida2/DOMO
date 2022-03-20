package com.example.waiterMain.domain.di

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
    }.also {
        provideFeatureOrderDeps()
        provideCurrentOrderDeps()
        provideProfileDeps()
    }

    private fun provideFeatureOrderDeps() {
        OrderDepsStore.deps = object : OrderAppComponentDeps {
            override val currentEmployee: Employee?
                get() = deps.currentEmployee
            override val ordersService: OrdersService<OrdersServiceSub>
                get() = deps.ordersService
        }
    }

    private fun provideCurrentOrderDeps() {
        CurrentOrderDepsStore.deps = object : CurrentOrdersAppComponentDeps {
            override val currentEmployee: Employee?
                get() = deps.currentEmployee
            override val ordersService: OrdersService<OrdersServiceSub>
                get() = deps.ordersService
        }
    }

    private fun provideProfileDeps() {
        ProfileDepsStore.deps = object : ProfileAppComponentDeps {
            override val currentEmployee: Employee?
                get() = deps.currentEmployee
            override val firebaseAuth: FirebaseAuth
                get() = profileDeps.firebaseAuth
        }
    }
}

interface ProfileModuleDeps {
    val firebaseAuth: FirebaseAuth
}
