package com.example.core.domain.interfaces

import com.example.core.domain.Employee
import java.io.Serializable

interface EmployeeAuthCallback: Serializable {
    fun onEmployeeLoggedIn(employee: Employee?)
}