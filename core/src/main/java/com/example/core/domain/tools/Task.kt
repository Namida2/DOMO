package com.example.core.domain.tools

import com.example.core.domain.entities.Employee
import com.example.core.domain.order.Order

typealias TaskWithEmployee = Task<Employee, Unit>
typealias TaskWithOrder = Task<Order, Unit>
typealias SimpleTask = Task<Unit, Unit>

interface Task<ASuccess, RSuccess> {
    fun onSuccess(result: ASuccess): RSuccess
    fun onError(message: ErrorMessage? = null)
}
