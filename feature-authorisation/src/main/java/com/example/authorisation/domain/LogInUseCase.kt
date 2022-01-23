package com.example.authorisation.domain

import com.example.authorisation.data.UsersRemoteRepository
import com.example.waiter_core.data.database.daos.EmployeeDao
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val employeeDao: EmployeeDao,
    private val usersRepository: UsersRemoteRepository
) {

}