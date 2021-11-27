package com.example.domo.models.remoteRepository

import com.example.domo.models.remoteRepository.FirestoreReferences.employeesCollectionRef
import com.example.domo.models.remoteRepository.FirestoreReferences.menuDocumentRef
import com.example.domo.models.remoteRepository.interfaces.SSRemoteRepositoryInterface
import com.example.domo.views.activities.log
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
import javax.inject.Inject


class SplashScreenRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) : SSRemoteRepositoryInterface {

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