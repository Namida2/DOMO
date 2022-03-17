package com.example.featureEmployees.domain.services

import com.example.core.domain.Employee
import kotlinx.coroutines.flow.MutableSharedFlow

interface EmployeesService {
    val employeesChanges: MutableSharedFlow<List<Employee>>
    fun setNewEmployeesList(employees: MutableList<Employee>)
    fun listenEmployeesPermissionChanges()
    fun removeEmployee(employee: Employee)
    fun cancel()
}