package com.example.domo.models.remoteRepository

import com.example.domo.views.log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import constants.FirestoreConstants
import entities.Employee
import javax.inject.Inject


class SplashScreenRemoteRepository @Inject constructor(
    private val fireStore: FirebaseFirestore
) {
    private val employeesCollectionRef: CollectionReference =
        fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS).document(FirestoreConstants.DOCUMENT_DOMO)
            .collection(FirestoreConstants.COLLECTION_EMPLOYEES)

    fun readCurrentEmployee (email: String, onComplete: (employee: Employee?) -> Unit) {
        employeesCollectionRef.document(email).get().addOnCompleteListener() { task ->
            if(task.isSuccessful) {
                val employee = task.result?.toObject<Employee>()
                onComplete(employee)
            }
            else {
                log("$this: ${task.exception}")
                onComplete(null)
            }
        }
    }
}