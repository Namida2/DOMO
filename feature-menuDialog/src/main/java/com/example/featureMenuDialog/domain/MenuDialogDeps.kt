package com.example.featureMenuDialog.domain

import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub

object MenuDialogDeps {
    lateinit var moduleDeps: MenuDialogModuleDeps
}

interface MenuDialogModuleDeps {
    val currentEmployee: Employee?
    val ordersService: OrdersService<OrdersServiceSub>
}
