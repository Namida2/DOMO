package com.example.featureEmployees.domain.useCases

import com.example.core.domain.Employee
import com.example.core.domain.tools.SimpleTask
import com.example.featureEmployees.domain.repositories.EmployeesRepository
import javax.inject.Inject

class DeleteEmployeeUseCase @Inject constructor(
    private val employeesRepository: EmployeesRepository
) {
    fun deleteEmployee(employee: Employee, task: SimpleTask) {
        employeesRepository.deleteEmployee(employee, task)
    }
}