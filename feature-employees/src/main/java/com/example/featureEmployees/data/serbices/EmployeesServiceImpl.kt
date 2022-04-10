package com.example.featureEmployees.data.serbices

import com.example.core.data.listeners.EmployeePermissionListener
import com.example.core.domain.entities.Employee
import com.example.featureEmployees.domain.di.EmployeesDepsStore
import com.example.featureEmployees.domain.listeners.NewEmployeesListener
import com.example.featureEmployees.domain.services.EmployeesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmployeesServiceImpl @Inject constructor(
    private val newEmployeesListener: NewEmployeesListener,
) : EmployeesService {
    private var coroutineScope: CoroutineScope = CoroutineScope(Main)
    private var employees = mutableListOf<Employee>()
    override val employeesChanges = MutableSharedFlow<List<Employee>>(replay = 1)

    override fun setNewEmployeesList(employees: MutableList<Employee>) {
        employees.indexOfFirst {
            it.email == EmployeesDepsStore.deps.currentEmployee?.email
        }.also {
            if(it == -1) return@also
            employees.removeAt(it)
        }
        this.employees = employees
        employeesChanges.tryEmit(employees)
    }

    override fun listenPermissionChanges() {
        coroutineScope.launch {
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
        listenNewEmployees()
    }

    private fun listenNewEmployees() {
        coroutineScope.launch {
            newEmployeesListener.newEmployeesFlow.collect { newEmployee ->
                if(newEmployee.email == EmployeesDepsStore.deps.currentEmployee?.email)
                    return@collect
                employees.find {
                    it.email == newEmployee.email
                } ?: employees.add(newEmployee).also {
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
        coroutineScope.cancel()
    }

}