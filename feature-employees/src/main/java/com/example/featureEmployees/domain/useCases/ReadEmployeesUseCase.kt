package com.example.featureEmployees.domain.useCases

import com.example.core.domain.entities.tools.SimpleTask
import com.example.featureEmployees.domain.repositories.EmployeesRepository
import javax.inject.Inject

class ReadEmployeesUseCase @Inject constructor(
    private val employeesRepository: EmployeesRepository
) {
    fun readEmployees(task: SimpleTask) {
        employeesRepository.readAllEmployees(task)
    }
}