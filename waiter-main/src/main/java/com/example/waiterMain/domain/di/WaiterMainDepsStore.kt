package com.example.waiterMain.domain.di

import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrdersServiceSub
import com.example.waiterCore.domain.tools.constants.OtherStringConstants.currentEmployeeIsNotSet
import com.example.waiterCore.domain.tools.extensions.logD
import java.lang.IllegalStateException

object WaiterMainDepsStore {
    var currentEmployee: Employee? = null
    get() = when (field) {
            null -> throw IllegalStateException(currentEmployeeIsNotSet)
            else -> field
        }
    lateinit var deps: WaiterMainDeps
}
