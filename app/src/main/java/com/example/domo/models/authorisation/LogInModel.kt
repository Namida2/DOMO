package com.example.domo.models.authorisation

import com.example.core.domain.entities.Employee
import com.example.core.domain.tools.TaskWithEmployee
import com.example.domo.models.interfaces.LogInModelInterface
import com.example.domo.models.remoteRepository.authorisation.LogInRemoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogInModel @Inject constructor(
    private val employeeDao: com.example.core.data.database.daos.EmployeeDao,
    private val remoteRepository: LogInRemoteRepository,
) : LogInModelInterface {
    override fun signIn(
        email: String,
        password: String,
        task: TaskWithEmployee
    ) {
        remoteRepository.signIn(email, password, object :
            TaskWithEmployee {
            override fun onSuccess(result: Employee) {
                CoroutineScope(IO).launch {
                    employeeDao.deleteAll()
                    employeeDao.insert(result)
                    withContext(Main) {
                        task.onSuccess(result)
                    }
                }
            }

            override fun onError(message: com.example.core.domain.tools.ErrorMessage?) {
                task.onError(message)
            }

        })
    }
}