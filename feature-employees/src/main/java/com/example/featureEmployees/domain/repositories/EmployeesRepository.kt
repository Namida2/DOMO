package com.example.featureEmployees.domain.repositories

import com.example.core.domain.entities.Employee
import com.example.core.domain.tools.SimpleTask

interface EmployeesRepository {
    fun readAllEmployees(task: SimpleTask)
    fun setPermissionForEmployee(employee: Employee, permission: Boolean, task: SimpleTask)
    fun deleteEmployee(employee: Employee, task: SimpleTask)
}