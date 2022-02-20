package com.example.waiterCore.domain.tools

import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.order.Order

typealias TaskWithEmployee = Task<Employee, Unit>
typealias TaskWithOrder = Task<Order, Unit>
typealias SimpleTask = Task<Unit, Unit>
interface Task<ASuccess, RSuccess> {
    fun onSuccess(arg: ASuccess): RSuccess
    fun onError(message: ErrorMessage? = null)
}
