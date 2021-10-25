package com.example.domo.models

import android.view.View
import com.example.domo.models.remoteRepository.RegistrationRemoteRepository
import com.example.domo.views.PostItem
import constants.EmployeePosts
import database.EmployeeDao
import entities.Employee
import tools.ErrorMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationModel @Inject constructor(
    private val employeeDao: EmployeeDao,
    private val remoteRepository: RegistrationRemoteRepository
    ) {

    fun registration(employee: Employee, onSuccess: () -> Unit, onError: (errorMessage: ErrorMessage) -> Unit) {
        remoteRepository.registration(employee, onSuccess, onError)
    }

    fun getPostItems(): MutableList<PostItem> =
        mutableListOf(
            PostItem(EmployeePosts.COOK, View.VISIBLE),
            PostItem(EmployeePosts.WAITER, View.INVISIBLE),
            PostItem(EmployeePosts.ADMINISTRATOR, View.INVISIBLE)
        )
}