package com.example.domo.models.remoteRepository

import com.example.domo.views.log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference

import com.google.firebase.firestore.FirebaseFirestore
import constants.FirestoreConstants.COLLECTION_EMPLOYEES
import constants.FirestoreConstants.COLLECTION_RESTAURANTS
import constants.FirestoreConstants.DOCUMENT_DOMO
import entities.Employee
import javax.inject.Inject

class RegistrationRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val employeesCollectionRef: CollectionReference =
        firestore.collection(COLLECTION_RESTAURANTS).document(DOCUMENT_DOMO)
            .collection(COLLECTION_EMPLOYEES)

    fun registration(employee: Employee, onSuccess: () -> Unit, onError: () -> Unit) {
        firestore.runTransaction { transition ->
            transition.set(employeesCollectionRef.document(employee.email), employee )
//            auth.createUserWithEmailAndPassword(employee.email, employee.password).addOnCompleteListener { task ->
//                if(task.isSuccessful) {
//                    transition.set(employeesCollectionRef.document(employee.email), employee )
//                } else {
//                    log(task.exception.toString())
//                }
//            }
        }
    }

}

