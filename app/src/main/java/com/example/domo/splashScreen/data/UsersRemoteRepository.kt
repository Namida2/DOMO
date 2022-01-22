package com.example.domo.splashScreen.data

import com.example.waiter_core.data.database.daos.EmployeeDao
import com.example.waiter_core.domain.Employee
import com.example.waiter_core.domain.tools.FirestoreReferences.employeesCollectionRef
import com.example.waiter_core.domain.tools.Task
import com.example.waiter_core.domain.tools.TaskWithEmployee
import com.example.waiter_core.domain.tools.extensions.logE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
        employeesCollectionRef.document(currentUser.email.toString()).get().addOnSuccessListener { response ->
            val str = currentUser.email
            val a = str?.length
            val employee = response.toObject<Employee>()
            if(employee == null) {
                task.onError()
                return@addOnSuccessListener
            }
            saveNewEmployeeData(employee, task)
        }.addOnFailureListener {
            logE("$this: ${it.message}")
            task.onError()
        }
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
//        currentUser.reload().addOnCompleteListener { firebaseAuthTask ->
//            if (firebaseAuthTask.isSuccessful) {
//                CoroutineScope(Dispatchers.Main).launch {
//                    val readEmployee = async(Dispatchers.IO) {
//                        return@async employeeDao.readCurrentEmployee()
//                    }
//                    val employee = readEmployee.await()
//                    if (employee == null)
//                        if (currentUser.email != null)
//                            readCurrentEmployee(currentUser.email!!) {
//                                if (it == null) {
//                                    auth.signOut()
//                                    task.onError()
//                                } else task.onSuccess(it)
//                            }
//                        else task.onError()
//                    else task.onSuccess(employee)
//                }
//            } else {
//                logE("$this: ${firebaseAuthTask.exception.toString()}")
//                task.onError()
//            }
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
