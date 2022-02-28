package com.example.domo.models.authorisation

import android.view.View
import com.example.core.domain.Employee
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.constants.EmployeePosts
import com.example.domo.models.interfaces.RegistrationModelInterface
import com.example.domo.models.remoteRepository.authorisation.RegistrationRemoteRepository
import entities.PostItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationModel @Inject constructor(
    private val employeeDao: com.example.core.data.database.daos.EmployeeDao,
    private val remoteRepository: RegistrationRemoteRepository,
) : RegistrationModelInterface {
    override fun registration(
        employee: Employee,
        task: com.example.core.domain.tools.TaskWithEmployee
    ) {
        remoteRepository.registration(employee, object :
            com.example.core.domain.tools.TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                CoroutineScope(IO).launch {
                    employeeDao.deleteAll()
                    employeeDao.insert(employee)
                    withContext(Main) {
                        task.onSuccess(employee)
                    }
                }
            }

            override fun onError(message: ErrorMessage?) {
                task.onError(message)
            }
        })
    }

    override fun getPostItems(): MutableList<PostItem> =
        mutableListOf(
            PostItem(EmployeePosts.COOK, View.VISIBLE),
            PostItem(EmployeePosts.WAITER, View.INVISIBLE),
            PostItem(EmployeePosts.ADMINISTRATOR, View.INVISIBLE)
        )
}