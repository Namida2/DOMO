package com.example.featureRegistration.domain.useCases

import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.core.domain.entities.tools.constants.EmployeePosts
import com.example.featureRegistration.data.RegistrationRemoteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestoreSettings
import javax.inject.Inject

class RegistrationUseCaseImpl @Inject constructor(
    private val remoteRepository: RegistrationRemoteRepository,
) : RegistrationUseCase {

    override fun registration(employee: Employee, task: TaskWithEmployee) {
        remoteRepository.registration(employee, task)
    }

    override fun logInAsAdministrator(employee: Employee, task: TaskWithEmployee) {
        remoteRepository.logInAsAdministrator(employee, task)
    }
}

interface RegistrationUseCase {
    fun registration(employee: Employee, task: TaskWithEmployee)
    fun logInAsAdministrator(employee: Employee, task: TaskWithEmployee)
}