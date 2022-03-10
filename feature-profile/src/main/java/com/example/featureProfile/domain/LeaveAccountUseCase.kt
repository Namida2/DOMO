package com.example.featureProfile.domain

import com.example.core.domain.tools.SimpleTask
import com.example.core.domain.tools.extensions.getExceptionMessage
import com.example.core.domain.tools.extensions.logE
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LeaveAccountUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : LeaveAccountUseCase {
    override fun leaveThisAccount(task: SimpleTask) {
        val currentEmployee = ProfileDepsStore.deps.currentEmployee!!
        firebaseAuth.signInWithEmailAndPassword(currentEmployee.email, currentEmployee.password)
            .addOnSuccessListener {
                firebaseAuth.signOut()
                task.onSuccess(Unit)
            }.addOnFailureListener {
                logE("$this: ${it.message}")
                task.onError(it.getExceptionMessage())
            }
    }
}

interface LeaveAccountUseCase {
    fun leaveThisAccount(task: SimpleTask)
}