package com.example.feature_splashscreen.domain

import com.example.feature_splashscreen.data.UsersRemoteRepository
import com.example.waiter_core.domain.tools.TaskWithEmployee
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
        //readEmployeeData(currentUser, task)
    }

}

interface GetCurrentEmployeeUseCase {
    fun getCurrentEmployee(task: TaskWithEmployee)
}