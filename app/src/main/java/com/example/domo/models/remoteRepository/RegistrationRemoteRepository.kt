package com.example.domo.models.remoteRepository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference

import com.google.firebase.firestore.FirebaseFirestore
import constants.FirestoreConstants.COLLECTION_EMPLOYEES
import constants.FirestoreConstants.COLLECTION_RESTAURANTS
import constants.FirestoreConstants.DOCUMENT_DOMO
import entities.Employee
import javax.inject.Inject


sealed class RegistrationResults {
    object Success: RegistrationResults()
    //object Error(): RegistrationResults()
}

class RegistrationRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val employeesCollectionRef: CollectionReference =
        firestore.collection(COLLECTION_RESTAURANTS).document(DOCUMENT_DOMO)
            .collection(COLLECTION_EMPLOYEES)

    fun registration(email: String, password: String, emloyee: Employee, result: (result: RegistrationResults) -> Unit) {
        firestore.runTransaction { transition ->
            auth.createUserWithEmailAndPassword(email, password).addOnCanceledListener {
               //transition.set(employeesCollectionRef.document(email), )

            }
        }
    }

}

