package com.example.featureEmployees.domain

import com.example.core.domain.Employee
import kotlinx.coroutines.flow.MutableSharedFlow

object EmployeesService {
    private var employees = mutableListOf<Employee>()
    val employeesChanges = MutableSharedFlow<List<Employee>>(replay = 1)

    fun setNewEmployeesList(employees: MutableList<Employee>) {
        this.employees = employees
        employeesChanges.tryEmit(employees)
    }
}