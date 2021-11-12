package com.example.domo.models

import com.example.domo.models.remoteRepository.LogInRemoteRepository
import database.daos.EmployeeDao
import entities.Employee
import entities.ErrorMessage
import entities.TaskWithEmployee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LogInModel @Inject constructor(
    private val employeeDao: EmployeeDao,
    private val remoteRepository: LogInRemoteRepository
) {
    fun signIn(email: String, password: String, task: TaskWithEmployee) {
        remoteRepository.signIn(email, password, object: TaskWithEmployee {
            override fun onSuccess(employee: Employee?) {
                CoroutineScope(IO).launch {
                    employeeDao.deleteAll()
                    employeeDao.insert(employee!!)
                    withContext(Main) {
                        task.onSuccess(employee)
                    }
                }
            }
            override fun onError(arg: ErrorMessage) {
               task.onError(arg)
            }
        })
    }
}