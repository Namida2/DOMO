package com.example.domo.registration.domain

import com.example.core.domain.Employee
import com.example.domo.registration.data.RegistrationRemoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegistrationUseCaseImpl @Inject constructor(
    private val remoteRepository: RegistrationRemoteRepository,
    private val employeeDao: com.example.core.data.database.daos.EmployeeDao
) : RegistrationUseCase {

    override fun registration(
        employee: Employee,
        task: com.example.core.domain.tools.TaskWithEmployee
    ) {
        remoteRepository.registration(employee, object :
            com.example.core.domain.tools.TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                CoroutineScope(Dispatchers.IO).launch {
                    employeeDao.deleteAll()
                    employeeDao.insert(employee)
                    withContext(Dispatchers.Main) {
                        task.onSuccess(employee)
                    }
                }
            }

            override fun onError(message: com.example.core.domain.tools.ErrorMessage?) {
                task.onError(message)
            }
        })
    }
}

interface RegistrationUseCase {
    fun registration(employee: Employee, task: com.example.core.domain.tools.TaskWithEmployee)
}