package com.example.feature_splashscreen.domain

import com.example.feature_splashscreen.data.UsersRemoteRepositiry
import com.example.waiter_core.data.database.daos.EmployeeDao
import com.example.waiter_core.domain.Employee
import com.example.waiter_core.domain.tools.Task
import com.example.waiter_core.domain.tools.TaskWithEmployee
import com.example.waiter_core.domain.tools.extentions.logE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetCurrentEmployeeUseCaseImpl @Inject constructor(
    private val usersRemoteRepository: UsersRemoteRepositiry
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