package com.example.featureEmployees.domain.listeners

import com.example.core.domain.Employee
import kotlinx.coroutines.flow.Flow

interface NewEmployeesListener {
    val newEmployeesFlow: Flow<Employee>
}