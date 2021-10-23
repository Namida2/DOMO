package com.example.domo.models

import com.example.domo.models.remoteRepository.SplashScreenRemoteRepository
import database.EmployeeDao
import entities.Employee
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashScreenModel @Inject constructor(
    private var employeeDao: EmployeeDao,
    private var remoteRepository: SplashScreenRemoteRepository
) {
    suspend fun getCurrentEmployee(): Employee? =
        employeeDao.readCurrentEmployee() //

}