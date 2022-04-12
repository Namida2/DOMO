package com.example.featureLogIn.data

import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.core.domain.entities.tools.constants.FirestoreConstants
import com.example.core.domain.entities.tools.constants.FirestoreReferences
import com.example.core.domain.entities.tools.constants.Messages
import com.example.core.domain.entities.tools.constants.Messages.permissionDeniedMessage
import com.example.core.domain.entities.tools.constants.Messages.wrongEmailOrPassword
import com.example.core.domain.entities.tools.extensions.addOnSuccessListenerWithDefaultFailureHandler
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.domain.entities.tools.extensions.readEmployeeByEmail
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

interface UsersRemoteRepository {
    fun logIn(email: String, password: String, task: TaskWithEmployee)
    fun logInAsAdministrator(employee: Employee, task: TaskWithEmployee)
}

class UsersRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : UsersRemoteRepository {

    override fun logIn(
        email: String,
        password: String,
        task: TaskWithEmployee
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                readCurrentEmployee(email, task)
            } else {
                logE("$this: ${it.exception}")
                task.onError(wrongEmailOrPassword)
            }
        }
    }

    private fun readCurrentEmployee(
        email: String,
        task: TaskWithEmployee
    ) {
        email.readEmployeeByEmail(this.toString(), object :
            TaskWithEmployee {
            override fun onSuccess(result: Employee) {
                if (!result.permission) {
                    auth.signOut()
                    task.onError(permissionDeniedMessage)
                } else task.onSuccess(result)
            }

            override fun onError(message: ErrorMessage?) {
                auth.signOut()
                task.onError(message)
            }
        })
    }

    override fun logInAsAdministrator(employee: Employee, task: TaskWithEmployee) {
        auth.signInWithEmailAndPassword(employee.email, employee.password).addOnCompleteListener {
            if (it.isSuccessful) {
                setPermissionForAdministrator(employee, task)
            } else {
                logE("$this: ${it.exception}")
                task.onError(wrongEmailOrPassword)
            }
        }
    }

    private fun setPermissionForAdministrator(employee: Employee, task: TaskWithEmployee) {
        FirestoreReferences.employeesCollectionRef.document(employee.email).update(
            FirestoreConstants.FIELD_PERMISSION, true
        ).addOnSuccessListenerWithDefaultFailureHandler(task) { task.onSuccess(employee) }
    }
}
