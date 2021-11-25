package com.example.domo.models.remoteRepository.authorisation

import com.example.domo.R
import com.example.domo.views.activities.log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import constants.FirestoreConstants
import entities.Employee
import entities.ErrorMessage
import entities.TaskWithEmployee

import javax.inject.Inject

class LogInRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) {
    private val employeesCollectionRef: CollectionReference =
        fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS)
            .document(FirestoreConstants.DOCUMENT_DOMO)
            .collection(FirestoreConstants.COLLECTION_EMPLOYEES)

    fun signIn(email: String, password: String, task: TaskWithEmployee) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                readCurrentEmployee(email, task)
            } else {
                log("$this: ${it.exception}")
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
                if (employee == null) {
                    auth.signOut()
                    task.onError(
                        ErrorMessage(
                            R.string.permissionErrorTitle,
                            R.string.permissionErrorMessage
                        )
                    )
                }
                else task.onSuccess(employee)
            } else {
                log("$this: ${it.exception}")
                task.onError(
                    ErrorMessage(
                        R.string.defaultTitle,
                        R.string.defaultMessage
                    )
                )
            }
        }
    }

}
