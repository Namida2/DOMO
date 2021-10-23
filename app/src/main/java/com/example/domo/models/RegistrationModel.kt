package com.example.domo.models

import com.example.domo.models.remoteRepository.RegistrationRemoteRepository
import database.EmployeeDao
import entities.Employee
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationModel @Inject constructor(
    private val employeeDao: EmployeeDao,
    private val remoteRepository: RegistrationRemoteRepository
    ) {

    fun registration(email: String, password: String, employee: Employee) {
        //remoteRepository.registration(email, password, employee)
    }
}