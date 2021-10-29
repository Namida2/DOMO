package com.example.domo.models

import com.example.domo.models.remoteRepository.SplashScreenRemoteRepository
import com.google.firebase.auth.FirebaseAuth
import database.EmployeeDao
import entities.Employee
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashScreenModel @Inject constructor(
    private var auth: FirebaseAuth,
    private var employeeDao: EmployeeDao,
    private var remoteRepository: SplashScreenRemoteRepository
) {
    suspend fun getCurrentEmployee(onSuccess: (employee: Employee) -> Unit, onError: () -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onError()
            return
        }
        val employee = employeeDao.readCurrentEmployee()
        if (employee == null)
            if (currentUser.email != null)
                remoteRepository.readCurrentEmployee(currentUser.email!!) {
                    if (it == null) onError()
                    else onSuccess(it)
                }
            else onError()
        else onSuccess(employee)
    }

}