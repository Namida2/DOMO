package com.example.cookMain.domain.di

import com.example.core.domain.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub

object CookMainDepsStore {
    lateinit var deps: CookMainDeps
}

interface CookMainDeps {
    val currentEmployee: Employee?
    val ordersService: OrdersService<OrdersServiceSub>
}