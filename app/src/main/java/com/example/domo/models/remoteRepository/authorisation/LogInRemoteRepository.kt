package com.example.domo.models.remoteRepository.authorisation

import com.example.domo.R
import com.example.waiter_core.domain.tools.FirestoreReferences.employeesCollectionRef
import com.google.firebase.auth.FirebaseAuth
import com.example.waiter_core.domain.tools.ErrorMessage
import com.example.waiter_core.domain.tools.TaskWithEmployee
import com.example.waiter_core.domain.Employee
import com.example.waiter_core.domain.tools.extentions.logE
import javax.inject.Inject

class LogInRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
) {

    private val permissionErrorMessage = ErrorMessage(
        R.string.permissionErrorTitle,
        R.string.permissionErrorMessage
    )
    private val defaultErrorMessage = ErrorMessage(
        R.string.defaultTitle,
        R.string.defaultMessage
    )

    fun signIn(email: String, password: String, task: TaskWithEmployee) {
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
