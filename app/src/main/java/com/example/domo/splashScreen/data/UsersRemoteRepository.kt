package com.example.domo.splashScreen.data

import com.example.firebaseAuthCore.domain.extensions.readEmployeeByEmail
import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.TaskWithEmployee
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UsersRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
//    private val employeeDao: EmployeeDao,
) : UsersRemoteRepository {

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    //TODO: First Call this method. This is necessary to check the current permission // STOPPED //
    override fun readCurrentEmployee(currentUser: FirebaseUser, task: TaskWithEmployee) {
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

    override fun saveNewEmployeeData(newEmployee: Employee, task: TaskWithEmployee) {
        //TODO: Save the employee data if they differ
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
    fun readCurrentEmployee(currentUser: FirebaseUser, task: TaskWithEmployee)
    fun saveNewEmployeeData(newEmployee: Employee, task: TaskWithEmployee)
}
