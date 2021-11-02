package com.example.domo.models

import androidx.lifecycle.LiveData
import com.example.domo.R
import com.example.domo.models.remoteRepository.LogInRemoteRepository
import com.example.domo.viewModels.RegistrationViewModelStates
import database.EmployeeDao
import entities.ErrorMessage
import entities.TaskWithErrorMessage
import javax.inject.Inject


class LogInModel @Inject constructor(
    private val employeeDao: EmployeeDao,
    private val remoteRepository: LogInRemoteRepository
) {
    fun signIn(email: String, password: String, task: TaskWithErrorMessage) {
        remoteRepository.signIn(email, password, task)
    }

}