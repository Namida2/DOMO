package com.example.domo.models

import com.example.domo.models.remoteRepository.SplashScreenRemoteRepository
import com.example.domo.views.log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import database.EmployeeDao
import entities.Employee
import entities.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashScreenModel @Inject constructor(
    private var auth: FirebaseAuth,
    private var employeeDao: EmployeeDao,
    private var remoteRepository: SplashScreenRemoteRepository
) {
    fun getCurrentEmployee( task: Task<Employee, Unit, Unit> ){
        val currentUser = auth.currentUser
        if (currentUser == null) {
            task.onError(Unit)
            return
        }
        readEmployeeData(currentUser, task)
    }

    private fun readEmployeeData(currentUser: FirebaseUser, task: Task<Employee, Unit, Unit>) {
        currentUser.reload().addOnCompleteListener { firebaseAuthTask ->
            if(firebaseAuthTask.isSuccessful) {
                CoroutineScope(Main).launch {
                    val readEmployee = async(IO) {
                        return@async employeeDao.readCurrentEmployee()
                    }
                    val employee = readEmployee.await()
                    if (employee == null)
                        if (currentUser.email != null)
                            remoteRepository.readCurrentEmployee(currentUser.email!!) {
                                if (it == null) {
                                    auth.signOut()
                                    task.onError(Unit)
                                }
                                else task.onSuccess(it)
                            }
                        else task.onError(Unit)
                    else task.onSuccess(employee)
                }
            }
            else {
                log("$this: ${firebaseAuthTask.exception.toString()}")
                task.onError(Unit)
            }
        }
    }


}