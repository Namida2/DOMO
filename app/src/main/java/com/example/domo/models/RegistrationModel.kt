package com.example.domo.models

import android.view.View
import com.example.domo.models.remoteRepository.RegistrationRemoteRepository
import com.example.domo.views.PostItem
import constants.EmployeePosts
import database.EmployeeDao
import entities.Employee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tools.ErrorMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationModel @Inject constructor(
    private val employeeDao: EmployeeDao,
    private val remoteRepository: RegistrationRemoteRepository
    ) {

    fun registration(employee: Employee, onSuccess: () -> Unit, onError: (errorMessage: ErrorMessage) -> Unit) {
        remoteRepository.registration(employee, {
            registrationModelOnSuccess(onSuccess, employee)
        }, onError)
    }

    private fun registrationModelOnSuccess(viewModelOnSuccess: () -> Unit, employee: Employee) {
        CoroutineScope(IO).launch {
            employeeDao.insert(employee)
            withContext(Main) {
                viewModelOnSuccess()
            }
        }
    }

    fun getPostItems(): MutableList<PostItem> =
        mutableListOf(
            PostItem(EmployeePosts.COOK, View.VISIBLE),
            PostItem(EmployeePosts.WAITER, View.INVISIBLE),
            PostItem(EmployeePosts.ADMINISTRATOR, View.INVISIBLE)
        )
}