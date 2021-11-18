package com.example.domo.models.remoteRepository

import com.example.domo.models.remoteRepository.interfaces.SSRemoteRepositoryInterface
import com.example.domo.views.log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import constants.FirestoreConstants
import constants.FirestoreConstants.COLLECTION_DATA
import constants.FirestoreConstants.COLLECTION_RESTAURANTS
import constants.FirestoreConstants.DOCUMENT_DOMO
import constants.FirestoreConstants.DOCUMENT_MENU
import constants.FirestoreConstants.FIELD_MENU_VERSION
import entities.Employee
import entities.Task
import javax.inject.Inject


class SplashScreenRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) : SSRemoteRepositoryInterface {

    private val employeesCollectionRef: CollectionReference =
        fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS)
            .document(FirestoreConstants.DOCUMENT_DOMO)
            .collection(FirestoreConstants.COLLECTION_EMPLOYEES)
    private val menuDocumentRef =
        fireStore.collection(COLLECTION_RESTAURANTS).document(DOCUMENT_DOMO)
            .collection(COLLECTION_DATA).document(DOCUMENT_MENU)

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun readCurrentEmployee(email: String, onComplete: (employee: Employee?) -> Unit) {
        employeesCollectionRef.document(email).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val employee = task.result?.toObject<Employee>()
                onComplete(employee)
            } else {
                log("$this: ${task.exception}")
                onComplete(null)
            }
        }
    }

    override fun readMenuVersion(onSuccess: (version: Long) -> Unit) {
        menuDocumentRef.get().addOnSuccessListener {
            val menuVersion = it.data?.get(FIELD_MENU_VERSION)
            if (menuVersion != null) {
                onSuccess(menuVersion as Long)
            } else {
                log("$this: menuVersion is null")
            }
        }.addOnFailureListener {
            log("$this: ${it.message}")
        }
    }

    override fun signOut() {
        auth.signOut()
    }

}