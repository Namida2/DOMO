package com.example.domo.models

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.domo.models.interfaces.MenuHolder
import com.example.domo.models.interfaces.SplashScreenModelInterface
import com.example.domo.models.remoteRepository.interfaces.SSRemoteRepositoryInterface
import com.example.domo.views.log
import com.google.firebase.auth.FirebaseUser
import constants.FirestoreConstants.FIELD_MENU_VERSION
import database.daos.EmployeeDao
import database.daos.MenuDao
import entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashScreenModel @Inject constructor(
    private var menuDao: MenuDao,
    private var menuHolder: MenuHolder,
    private var employeeDao: EmployeeDao,
    private var sharedPreferences: SharedPreferences,
    private var remoteRepository: SSRemoteRepositoryInterface,
) : SplashScreenModelInterface {

    override fun readMenu() {
        remoteRepository.readMenuVersion(object : Task<Long, Unit> {
            override fun onSuccess(arg: Long) {
                if(isItTheSameMenuVersion(arg)) {

                } else {
                    readNewMenu()
                }
            }
            override fun onError(message: ErrorMessage?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun readNewMenu() {
        remoteRepository.readNewMenu(object : Task<List<Dish>, Unit> {
            override fun onSuccess(arg: List<Dish>) {
                TODO("Not yet implemented")
            }

            override fun onError(message: ErrorMessage?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun isItTheSameMenuVersion(version: Long): Boolean =
        sharedPreferences.getLong(FIELD_MENU_VERSION, 0) == version


    override fun getCurrentEmployee(task: TaskWithEmployee) {
        val currentUser = remoteRepository.getCurrentUser()
        if (currentUser == null) {
            task.onError()
            return
        }
        readEmployeeData(currentUser, task)
    }

    private fun readEmployeeData(currentUser: FirebaseUser, task: Task<Employee, Unit>) {
        currentUser.reload().addOnCompleteListener { firebaseAuthTask ->
            if (firebaseAuthTask.isSuccessful) {
                CoroutineScope(Main).launch {
                    val readEmployee = async(IO) {
                        return@async employeeDao.readCurrentEmployee()
                    }
                    val employee = readEmployee.await()
                    if (employee == null)
                        if (currentUser.email != null)
                            remoteRepository.readCurrentEmployee(currentUser.email!!) {
                                if (it == null) {
                                    remoteRepository.signOut()
                                    task.onError()
                                } else task.onSuccess(it)
                            }
                        else task.onError()
                    else task.onSuccess(employee)
                }
            } else {
                log("$this: ${firebaseAuthTask.exception.toString()}")
                task.onError()
            }
        }
    }
}