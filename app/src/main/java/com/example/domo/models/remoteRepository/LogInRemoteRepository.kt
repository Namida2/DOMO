package com.example.domo.models.remoteRepository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import entities.TaskWithErrorMessage
import javax.inject.Inject

class LogInRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) {
    fun signIn(email: String, password: String, task: TaskWithErrorMessage) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
            if(it.isSuccessful) {

            }

        }

    }

}
