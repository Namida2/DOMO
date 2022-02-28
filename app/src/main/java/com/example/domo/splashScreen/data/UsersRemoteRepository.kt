package com.example.domo.splashScreen.data

import com.example.core.domain.Employee
import com.example.core.domain.tools.extensions.readEmployeeByEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UsersRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
//    private val employeeDao: EmployeeDao,
) : UsersRemoteRepository {

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    //TODO: First Call this method. This is necessary to check the current permission // STOPPED //
    override fun readCurrentEmployee(
        currentUser: FirebaseUser,
        task: com.example.core.domain.tools.TaskWithEmployee
    ) {
        currentUser.email?.readEmployeeByEmail(this.toString(), object :
            com.example.core.domain.tools.TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                saveNewEmployeeData(arg, task)
            }

            override fun onError(message: com.example.core.domain.tools.ErrorMessage?) {
                auth.signOut()
                task.onError()
            }
        })
    }

    override fun saveNewEmployeeData(
        newEmployee: Employee,
        task: com.example.core.domain.tools.TaskWithEmployee
    ) {
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
    fun readCurrentEmployee(
        currentUser: FirebaseUser,
        task: com.example.core.domain.tools.TaskWithEmployee
    )

    fun saveNewEmployeeData(
        newEmployee: Employee,
        task: com.example.core.domain.tools.TaskWithEmployee
    )
}
