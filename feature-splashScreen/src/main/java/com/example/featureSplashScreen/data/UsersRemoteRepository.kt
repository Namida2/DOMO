package com.example.featureSplashScreen.data

import com.example.core.domain.Employee
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.TaskWithEmployee
import com.example.core.domain.tools.extensions.readEmployeeByEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UsersRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : UsersRemoteRepository {

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    //Check the current permission
    override fun readCurrentEmployee(
        currentUser: FirebaseUser,
        task: TaskWithEmployee
    ) {
        currentUser.email?.readEmployeeByEmail(this.toString(), object : TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                saveNewEmployeeData(arg, task)
            }
            override fun onError(message: ErrorMessage?) {
                auth.signOut()
                task.onError()
            }
        })
    }

    override fun saveNewEmployeeData(
        newEmployee: Employee,
        task: TaskWithEmployee
    ) {
//        CoroutineScope(Dispatchers.Main).launch {
//            val currentEmployee = employeeDao.readCurrentEmployee()
//            if (currentEmployee == null || currentEmployee != newEmployee) {
//                employeeDao.deleteAll()
//                employeeDao.insert(newEmployee)
//            }
//        }
        task.onSuccess(newEmployee)
    }
}

interface UsersRemoteRepository {
    fun getCurrentUser(): FirebaseUser?
    fun readCurrentEmployee(
        currentUser: FirebaseUser,
        task: TaskWithEmployee
    )

    fun saveNewEmployeeData(
        newEmployee: Employee,
        task: TaskWithEmployee
    )
}
