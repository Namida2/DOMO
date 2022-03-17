package com.example.featureLogIn.data

import com.example.core.domain.Employee
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.constants.ErrorMessages.permissionDeniedMessage
import com.example.core.domain.tools.constants.ErrorMessages.wrongEmailOrPassword
import com.example.core.domain.tools.TaskWithEmployee
import com.example.core.domain.tools.extensions.logE
import com.example.core.domain.tools.extensions.readEmployeeByEmail
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

interface UsersRemoteRepository {
    fun logIn(email: String, password: String, task: TaskWithEmployee)
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
            override fun onSuccess(arg: Employee) {
                if (!arg.permission) {
                    auth.signOut()
                    task.onError(permissionDeniedMessage)
                } else task.onSuccess(arg)
            }

            override fun onError(message: ErrorMessage?) {
                auth.signOut()
                task.onError(message)
            }
        })
    }
}
