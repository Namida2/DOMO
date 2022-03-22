package com.example.featureSplashScreen.data

import com.example.core.domain.entities.Employee
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.TaskWithEmployee
import com.example.core.domain.tools.extensions.readEmployeeByEmail
import com.example.featureSplashScreen.domain.repositories.UsersRemoteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UsersRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : UsersRemoteRepository {

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun readCurrentEmployee(
        currentUser: FirebaseUser,
        task: TaskWithEmployee
    ) {
        currentUser.email?.readEmployeeByEmail(this.toString(), object : TaskWithEmployee {
            override fun onSuccess(result: Employee) {
                task.onSuccess(result)
            }
            override fun onError(message: ErrorMessage?) {
                auth.signOut()
                task.onError(message)
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
//        task.onSuccess(newEmployee)
    }
}
