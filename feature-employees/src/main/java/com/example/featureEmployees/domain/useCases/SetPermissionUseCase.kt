package com.example.featureEmployees.domain.useCases

import com.example.core.domain.Employee
import com.example.core.domain.tools.SimpleTask
import com.example.featureEmployees.domain.repositories.EmployeesRepository
import javax.inject.Inject

class SetPermissionUseCase @Inject constructor(
    private val employeesRepository: EmployeesRepository
) {
    fun setPermissionForEmployee(employee: Employee, permission: Boolean, task: SimpleTask) {
        employeesRepository.setPermissionForEmployee(employee, permission, task)
    }
}