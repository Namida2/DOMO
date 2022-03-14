package com.example.featureEmployees.domain.repositories

import com.example.core.domain.tools.SimpleTask

interface EmployeesRepository {
    fun readAllEmployees(task: SimpleTask)
}