package com.example.featureLogIn.domain

import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.featureLogIn.data.UsersRemoteRepository
import javax.inject.Inject

class LogInUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRemoteRepository,
) : LogInUseCase {
    override fun logIn(email: String, password: String, task: TaskWithEmployee) {
        usersRepository.logIn(email, password, task)
    }

    override fun logInAsAdministrator(employee: Employee, task: TaskWithEmployee) {
        usersRepository.logInAsAdministrator(employee, task)
    }
}

interface LogInUseCase {
    fun logIn(email: String, password: String, task: TaskWithEmployee)
    fun logInAsAdministrator(employee: Employee, task: TaskWithEmployee)
}