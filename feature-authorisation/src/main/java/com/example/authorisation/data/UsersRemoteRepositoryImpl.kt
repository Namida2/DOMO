package com.example.authorisation.data

import com.example.authorisation.R
import com.example.firebase_auth_core.domain.extensions.readEmployeeByEmail
import com.example.waiter_core.domain.Employee
import com.example.waiter_core.domain.tools.ErrorMessage
import com.example.waiter_core.domain.tools.FirestoreReferences
import com.example.waiter_core.domain.tools.TaskWithEmployee
import com.example.waiter_core.domain.tools.extensions.logE
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UsersRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): UsersRemoteRepository {
    private val permissionErrorMessage = ErrorMessage(
        R.string.permissionErrorTitle,
        R.string.permissionErrorMessage
    )
    private val defaultErrorMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.defaultMessage
    )

    override fun logIn(email: String, password: String, task: TaskWithEmployee) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                readCurrentEmployee(email, task)
            } else {
                logE("$this: ${it.exception}")
                task.onError(
                    ErrorMessage(
                        R.string.wrongEmailOrPasswordTitle,
                        R.string.wrongEmailOrPasswordMessage
                    )
                )
            }
        }
    }

    private fun readCurrentEmployee(email: String, task: TaskWithEmployee) {
        email.readEmployeeByEmail(this.toString(), object : TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                if (!arg.permission) {
                    auth.signOut()
                    //TODO: Add a ViewModel // STOPPED //
                    task.onError(permissionErrorMessage)
                } else task.onSuccess(arg)
            }

            override fun onError(message: ErrorMessage?) {
                task.onError()
            }

        })

        FirestoreReferences.employeesCollectionRef.document(email).get().addOnCompleteListener {
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

interface UsersRemoteRepository {
    fun logIn(email: String, password: String, task: TaskWithEmployee)
}