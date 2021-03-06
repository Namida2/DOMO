package com.example.featureSplashScreen.domain.useCases

import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.core.domain.entities.tools.constants.Messages.employeeDoesNotExists
import com.example.featureSplashScreen.domain.repositories.UsersRemoteRepository
import javax.inject.Inject

class GetCurrentEmployeeUseCaseImpl @Inject constructor(
    private val usersRemoteRepository: UsersRemoteRepository
) : GetCurrentEmployeeUseCase {

    override fun getCurrentEmployee(task: TaskWithEmployee) {
        val currentUser = usersRemoteRepository.getCurrentUser()
        if (currentUser == null) {
            task.onError(employeeDoesNotExists)
            return
        }
        usersRemoteRepository.readCurrentEmployee(currentUser, task)
    }
}

interface GetCurrentEmployeeUseCase {
    fun getCurrentEmployee(task: TaskWithEmployee)
}