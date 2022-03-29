package com.example.core.domain.entities.tools

import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.order.Order

typealias TaskWithEmployee = Task<Employee, Unit>
typealias TaskWithOrder = Task<Order, Unit>
typealias SimpleTask = Task<Unit, Unit>

interface Task<A, R> {
    fun onSuccess(result: A): R
    fun onError(message: ErrorMessage? = null)
}
