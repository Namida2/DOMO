package com.example.featureEmployees.domain.useCases

import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import com.example.featureEmployees.domain.repositories.EmployeesRepository
import com.example.featureEmployees.domain.services.EmployeesService
import javax.inject.Inject

class DeleteEmployeeUseCase @Inject constructor(
    private val employeesRepository: EmployeesRepository,
    private val employeesService: EmployeesService
) {
    fun deleteEmployee(employee: Employee, task: SimpleTask) {
        employeesRepository.deleteEmployee(employee, object: SimpleTask {
            override fun onSuccess(result: Unit) {
                employeesService.removeEmployee(employee)
                task.onSuccess(Unit)
            }
            override fun onError(message: ErrorMessage?) {
                task.onError(message)
            }
        })

    }
}