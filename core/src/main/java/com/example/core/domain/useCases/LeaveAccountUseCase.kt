package com.example.core.domain.useCases

import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.extensions.getExceptionMessage
import com.example.core.domain.entities.tools.extensions.logE
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LeaveAccountUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun leaveThisAccount(employee: Employee, task: SimpleTask? = null) {
        firebaseAuth.signInWithEmailAndPassword(employee.email, employee.password)
            .addOnSuccessListener {
                firebaseAuth.signOut()
                task?.onSuccess(Unit)
            }.addOnFailureListener {
                logE("$this: ${it.message}")
                task?.onError(it.getExceptionMessage())
            }
    }

    fun leaveImmediately() {
        firebaseAuth.signOut()
    }
}
