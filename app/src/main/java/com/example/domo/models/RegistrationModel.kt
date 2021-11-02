package com.example.domo.models

import android.view.View
import com.example.domo.models.remoteRepository.RegistrationRemoteRepository
import constants.EmployeePosts
import database.EmployeeDao
import entities.Employee
import entities.ErrorMessage
import entities.PostItem
import entities.TaskWithErrorMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationModel @Inject constructor(
    private val employeeDao: EmployeeDao,
    private val remoteRepository: RegistrationRemoteRepository
) {

    fun registration(employee: Employee, task: TaskWithErrorMessage) {
        remoteRepository.registration(employee, object : TaskWithErrorMessage {
            override fun onSuccess(arg: Unit) {
                CoroutineScope(IO).launch {
                    employeeDao.insert(employee)
                    withContext(Main) {
                        task.onSuccess(Unit)
                    }
                }
            }

            override fun onError(message: ErrorMessage) {
                task.onError(message)
            }
        })
    }

    fun getPostItems(): MutableList<PostItem> =
        mutableListOf(
            PostItem(EmployeePosts.COOK, View.VISIBLE),
            PostItem(EmployeePosts.WAITER, View.INVISIBLE),
            PostItem(EmployeePosts.ADMINISTRATOR, View.INVISIBLE)
        )
}