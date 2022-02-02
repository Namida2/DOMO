package com.example.domo.registration.domain

import com.example.domo.registration.data.RegistrationRemoteRepository
import com.example.waiterCore.data.database.daos.EmployeeDao
import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.TaskWithEmployee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegistrationUseCaseImpl @Inject constructor(
    private val remoteRepository: RegistrationRemoteRepository,
    private val employeeDao: EmployeeDao
) : RegistrationUseCase {

    override fun registration(employee: Employee, task: TaskWithEmployee) {
        remoteRepository.registration(employee, object : TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                CoroutineScope(Dispatchers.IO).launch {
                    employeeDao.deleteAll()
                    employeeDao.insert(employee)
                    withContext(Dispatchers.Main) {
                        task.onSuccess(employee)
                    }
                }
            }
            override fun onError(message: ErrorMessage?) {
                task.onError(message)
            }
        })
    }
}

interface RegistrationUseCase {
    fun registration(employee: Employee, task: TaskWithEmployee)
}