package com.example.featureSplashScreen.domain

import com.example.core.domain.tools.TaskWithEmployee
import com.example.featureSplashScreen.data.UsersRemoteRepository
import javax.inject.Inject

class GetCurrentEmployeeUseCaseImpl @Inject constructor(
    private val usersRemoteRepository: UsersRemoteRepository
) : GetCurrentEmployeeUseCase {

    override fun getCurrentEmployee(task: TaskWithEmployee) {
        val currentUser = usersRemoteRepository.getCurrentUser()
        if (currentUser == null) {
            task.onError()
            return
        }
        usersRemoteRepository.readCurrentEmployee(currentUser, task)
    }
}

interface GetCurrentEmployeeUseCase {
    fun getCurrentEmployee(task: TaskWithEmployee)
}