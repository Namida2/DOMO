package com.example.cookMain.domain.di

import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.entities.order.OrdersServiceSub
import com.example.core.domain.interfaces.NewMenuVersionCallback
import com.example.core.domain.listeners.MenuVersionListener
import com.google.firebase.auth.FirebaseAuth

object CookMainDepsStore {
    lateinit var deps: CookMainDeps
    lateinit var employeeAuthCallback: EmployeeAuthCallback
    lateinit var newMenuVersionCallback: NewMenuVersionCallback
    lateinit var appComponent: CookMainAppComponent
}

interface CookMainDeps {
    val currentEmployee: Employee?
    val firebaseAuth: FirebaseAuth
    val ordersService: OrdersService
}