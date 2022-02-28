package com.example.domo.authorization.data

import com.example.core.domain.Employee
import com.example.core.domain.tools.ErrorMessages.permissionErrorMessage
import com.example.core.domain.tools.ErrorMessages.wrongEmailOrPassword
import com.example.core.domain.tools.extensions.logE
import com.example.core.domain.tools.extensions.readEmployeeByEmail
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

interface UsersRemoteRepository {
    fun logIn(email: String, password: String, task: com.example.core.domain.tools.TaskWithEmployee)
}

class UsersRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : UsersRemoteRepository {

    override fun logIn(
        email: String,
        password: String,
        task: com.example.core.domain.tools.TaskWithEmployee
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
        task: com.example.core.domain.tools.TaskWithEmployee
    ) {
        email.readEmployeeByEmail(this.toString(), object :
            com.example.core.domain.tools.TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                if (!arg.permission) {
                    auth.signOut()
                    task.onError(permissionErrorMessage)
                } else task.onSuccess(arg)
            }

            override fun onError(message: com.example.core.domain.tools.ErrorMessage?) {
                auth.signOut()
                task.onError(message)
            }
        })
    }
}
