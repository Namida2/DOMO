package com.example.featureMenuDialog.domain

import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.OrdersService

object MenuDialogDepsStore {
    lateinit var deps: MenuDialogDeps
}

interface MenuDialogDeps {
    val currentEmployee: Employee?
    val ordersService: OrdersService
}
