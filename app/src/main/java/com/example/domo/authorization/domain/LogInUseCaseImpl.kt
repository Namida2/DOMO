package com.example.domo.authorization.domain

import com.example.domo.authorization.data.UsersRemoteRepository
import com.example.waiterCore.data.database.daos.EmployeeDao
import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.TaskWithEmployee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogInUseCaseImpl @Inject constructor(
    //private val employeeDao: EmployeeDao,
    private val usersRepository: UsersRemoteRepository,
) : LogInUseCase {
    override fun logIn(email: String, password: String, task: TaskWithEmployee) {
        usersRepository.logIn(email, password, task)
    }
}

interface LogInUseCase {
    fun logIn(email: String, password: String, task: TaskWithEmployee)
}