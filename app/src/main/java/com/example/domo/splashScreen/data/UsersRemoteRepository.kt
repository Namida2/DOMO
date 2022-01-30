package com.example.domo.splashScreen.data

import com.example.firebase_auth_core.domain.extensions.readEmployeeByEmail
import com.example.waiterCore.data.database.daos.EmployeeDao
import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.TaskWithEmployee
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UsersRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val employeeDao: EmployeeDao,
) : UsersRemoteRepository {

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    //TODO: First Call this method. This is necessary to check the current permission // STOPPED //
    //TODO: Implement the authorisation and registration module
    override fun readCurrentEmployee(currentUser: FirebaseUser, task: TaskWithEmployee) {
        currentUser.email?.readEmployeeByEmail(this.toString(), object : TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                saveNewEmployeeData(arg, task)
            }
            override fun onError(message: ErrorMessage?) {
                task.onError()
            }
        })
    }

    override fun saveNewEmployeeData(newEmployee: Employee, task: TaskWithEmployee) {
        //TODO: Save the employee data if they differ

        CoroutineScope(Dispatchers.Main).launch {
            val currentEmployee = employeeDao.readCurrentEmployee()
            if (currentEmployee == null || currentEmployee != newEmployee) {
                employeeDao.deleteAll()
                employeeDao.insert(newEmployee)
            }
            task.onSuccess(newEmployee)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

}

interface UsersRemoteRepository {
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
    fun readCurrentEmployee(currentUser: FirebaseUser, task: TaskWithEmployee)
    fun saveNewEmployeeData(newEmployee: Employee, task: TaskWithEmployee)
}
