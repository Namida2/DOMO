package com.example.domo.models.remoteRepository.authorisation

import com.example.core.domain.entities.Employee
import com.example.core.domain.tools.constants.FirestoreReferences.employeesCollectionRef
import com.example.core.domain.tools.extensions.logE
import com.example.domo.R
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LogInRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
) {
    private val permissionErrorMessage = com.example.core.domain.tools.ErrorMessage(
        R.string.permissionDeniedTitle,
        R.string.permissionDeniedMessage
    )
    private val defaultErrorMessage = com.example.core.domain.tools.ErrorMessage(
        R.string.defaultTitle,
        R.string.defaultMessage
    )

    fun signIn(
        email: String,
        password: String,
        task: com.example.core.domain.tools.TaskWithEmployee
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                readCurrentEmployee(email, task)
            } else {
                logE("$this: ${it.exception}")
                task.onError(
                    com.example.core.domain.tools.ErrorMessage(
                        R.string.wrongEmailOrPasswordTitle,
                        R.string.wrongEmailOrPasswordMessage
                    )
                )
            }
        }
    }

    private fun readCurrentEmployee(
        email: String,
        task: com.example.core.domain.tools.TaskWithEmployee
    ) {
        employeesCollectionRef.document(email).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val employee = it.result?.toObject(Employee::class.java)
                if (employee == null) {
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
