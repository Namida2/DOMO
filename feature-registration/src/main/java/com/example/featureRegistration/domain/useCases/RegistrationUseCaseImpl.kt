package com.example.featureRegistration.domain.useCases

import com.example.core.domain.Employee
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.TaskWithEmployee
import com.example.featureRegistration.data.RegistrationRemoteRepository
import javax.inject.Inject

class RegistrationUseCaseImpl @Inject constructor(
    private val remoteRepository: RegistrationRemoteRepository,
//    private val employeeDao: EmployeeDao
) : RegistrationUseCase {

    override fun registration(
        employee: Employee,
        task: TaskWithEmployee
    ) {
        remoteRepository.registration(employee, object :
            TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                task.onSuccess(employee)
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