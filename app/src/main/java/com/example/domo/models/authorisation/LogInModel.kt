package com.example.domo.models.authorisation

import com.example.domo.models.interfaces.LogInModelInterface
import com.example.domo.models.remoteRepository.authorisation.LogInRemoteRepository
import com.example.waiterCore.data.database.daos.EmployeeDao
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.TaskWithEmployee
import com.example.waiterCore.domain.Employee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LogInModel @Inject constructor(
    private val employeeDao: EmployeeDao,
    private val remoteRepository: LogInRemoteRepository,
) : LogInModelInterface {
    override fun signIn(email: String, password: String, task: TaskWithEmployee) {
        remoteRepository.signIn(email, password, object : TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                CoroutineScope(IO).launch {
                    employeeDao.deleteAll()
                    employeeDao.insert(arg)
                    withContext(Main) {
                        task.onSuccess(arg)
                    }
                }
            }
            override fun onError(message: ErrorMessage?) {
                task.onError(message)
            }

        })
    }
}