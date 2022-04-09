package com.example.featureRegistration.domain.useCases

import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.core.domain.entities.tools.constants.EmployeePosts
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
        if (employee.post == EmployeePosts.ADMINISTRATOR.value)
            employee.permission = true
        remoteRepository.registration(employee, object : TaskWithEmployee {
            override fun onSuccess(result: Employee) {
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