package com.example.core.domain.interfaces

import com.example.core.domain.Employee
import java.io.Serializable

interface EmployeeAuthorizationCallback: Serializable {
    fun onEmployeeLoggedIn(employee: Employee?)
}