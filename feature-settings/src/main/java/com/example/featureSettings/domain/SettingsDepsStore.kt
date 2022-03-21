package com.example.featureSettings.domain

import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub

object SettingsDepsStore {
    lateinit var deps: SettingsDeps
}

interface SettingsDeps {
    val currentEmployee: Employee?
    val ordersService: OrdersService<OrdersServiceSub>
}