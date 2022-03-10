package com.example.cookMain.domain.di

import com.example.core.domain.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.google.firebase.auth.FirebaseAuth

object CookMainDepsStore {
    lateinit var deps: CookMainDeps
}

interface CookMainDeps {
    val currentEmployee: Employee?
    val firebaseAuth: FirebaseAuth
    val ordersService: OrdersService<OrdersServiceSub>
}