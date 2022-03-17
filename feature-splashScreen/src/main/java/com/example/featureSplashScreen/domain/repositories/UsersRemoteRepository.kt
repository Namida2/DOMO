package com.example.featureSplashScreen.domain.repositories

import com.example.core.domain.Employee
import com.example.core.domain.tools.TaskWithEmployee
import com.google.firebase.auth.FirebaseUser

interface UsersRemoteRepository {
    fun getCurrentUser(): FirebaseUser?
    fun readCurrentEmployee(
        currentUser: FirebaseUser,
        task: TaskWithEmployee
    )

    fun saveNewEmployeeData(
        newEmployee: Employee,
        task: TaskWithEmployee
    )
}
