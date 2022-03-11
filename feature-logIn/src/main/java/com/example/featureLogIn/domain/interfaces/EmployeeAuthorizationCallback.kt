package com.example.featureLogIn.domain.interfaces

import android.os.Parcelable
import com.example.core.domain.Employee
import java.io.Serializable

interface EmployeeAuthorizationCallback: Serializable {
    fun onEmployeeLoggedIn(employee: Employee)
}