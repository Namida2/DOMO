package com.example.featureEmployees.data

import com.example.core.data.listeners.EmployeePermissionListener
import com.example.core.domain.Employee
import com.example.featureEmployees.domain.services.EmployeesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmployeesServiceImpl @Inject constructor(): EmployeesService {
    private var employeePermissionChangeJob: Job? = null
    private var employees = mutableListOf<Employee>()
    override val employeesChanges = MutableSharedFlow<List<Employee>>(replay = 1)

    override fun setNewEmployeesList(employees: MutableList<Employee>) {
        this.employees = employees
        employeesChanges.tryEmit(employees)
    }

    override fun listenEmployeesPermissionChanges() {
        employeePermissionChangeJob = CoroutineScope(Main).launch {
            EmployeePermissionListener.permissionChanges.collect { newPermission ->
                employees.indexOfFirst { employee ->
                    employee.email == newPermission.email
                }.let { index ->
                    if (index == -1) return@collect
                    employees[index] = employees[index].copy(permission = newPermission.permission)
                    employeesChanges.tryEmit(employees)
                }
            }
        }
    }

    override fun removeEmployee(employee: Employee) {
        employees.removeAt(
            employees.indexOfFirst {
                it.email == employee.email
            }
        )
        employeesChanges.tryEmit(employees)
    }

    override fun cancel() {
        employeePermissionChangeJob?.cancel()
    }

}