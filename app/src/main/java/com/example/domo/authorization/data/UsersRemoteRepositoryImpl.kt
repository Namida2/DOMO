package com.example.domo.authorization.data

import com.example.firebase_auth_core.domain.extensions.readEmployeeByEmail
import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.ErrorMessages.defaultErrorMessage
import com.example.waiterCore.domain.tools.ErrorMessages.networkConnectionMessage
import com.example.waiterCore.domain.tools.ErrorMessages.permissionErrorMessage
import com.example.waiterCore.domain.tools.ErrorMessages.wrongEmailOrPassword
import com.example.waiterCore.domain.tools.FirestoreReferences.employeesCollectionRef
import com.example.waiterCore.domain.tools.TaskWithEmployee
import com.example.waiterCore.domain.tools.extensions.logE
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

interface UsersRemoteRepository {
    fun logIn(email: String, password: String, task: TaskWithEmployee)
}

class UsersRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): UsersRemoteRepository {

    override fun logIn(email: String, password: String, task: TaskWithEmployee) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                readCurrentEmployee(email, task)
            } else {
                logE("$this: ${it.exception}")
                task.onError(wrongEmailOrPassword)
            }
        }
    }

    private fun readCurrentEmployee(email: String, task: TaskWithEmployee) {
        email.readEmployeeByEmail(this.toString(), object : TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                if (!arg.permission) {
                    auth.signOut()
                    task.onError(permissionErrorMessage)
                } else task.onSuccess(arg)
            }

            override fun onError(message: ErrorMessage?) {
                task.onError(message)
            }
        })
        employeesCollectionRef.document(email).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val employee = it.result?.toObject(Employee::class.java)
                if(employee == null) {
                    task.onError(defaultErrorMessage)
                    return@addOnCompleteListener
                }
                if (!employee.permission) {
                    auth.signOut()
                    task.onError(permissionErrorMessage)
                } else task.onSuccess(employee)
            } else {
                logE("$this: ${it.exception}")
                task.onError(defaultErrorMessage)
            }
        }
    }
}
