package com.example.domo.models

import android.content.SharedPreferences
import com.example.domo.models.interfaces.MenuLocalRepository
import com.example.domo.models.interfaces.SplashScreenModelInterface
import com.example.domo.models.remoteRepository.interfaces.SSRemoteRepositoryInterface
import com.google.firebase.auth.FirebaseUser
import constants.FirestoreConstants.FIELD_MENU_VERSION
import database.daos.EmployeeDao
import entities.*
import entities.tools.Task
import entities.tools.TaskWithEmployee
import extentions.logD
import extentions.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


//TODO: Add the OrderDao and reading newOrders if they exist // STOPPED 1 //
@Singleton
class SplashScreenModel @Inject constructor(
    private var menuHolder: MenuLocalRepository,
    private var employeeDao: EmployeeDao,
    private var sharedPreferences: SharedPreferences,
    private var remoteRepository: SSRemoteRepositoryInterface,
) : SplashScreenModelInterface {

    override fun readMenu() {
        remoteRepository.readMenuVersion { version ->
            if (isItTheSameMenuVersion(version)) {
                logD("$this: The same menu")
                menuHolder.readExitingMenu()
            } else {
                logD("$this: New menu")
                menuHolder.readNewMenu {
                    sharedPreferences.edit().putLong(FIELD_MENU_VERSION, version).apply()
                }
            }
        }
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
                logE("$this: ${firebaseAuthTask.exception.toString()}")
                task.onError()
            }
        }
    }

}