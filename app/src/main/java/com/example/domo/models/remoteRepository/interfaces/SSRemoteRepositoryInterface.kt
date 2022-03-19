package com.example.domo.models.remoteRepository.interfaces

import com.example.core.domain.entities.Employee
import com.google.firebase.auth.FirebaseUser

interface SSRemoteRepositoryInterface {
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
    fun readMenuVersion(onSuccess: (version: Long) -> Unit)
    fun readCurrentEmployee(email: String, onComplete: (employee: Employee?) -> Unit)
}