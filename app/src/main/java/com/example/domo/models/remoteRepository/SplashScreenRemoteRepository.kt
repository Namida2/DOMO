package com.example.domo.models.remoteRepository

import com.example.domo.models.remoteRepository.FirestoreReferences.employeesCollectionRef
import com.example.domo.models.remoteRepository.FirestoreReferences.menuDocumentRef
import com.example.domo.models.remoteRepository.interfaces.SSRemoteRepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import constants.FirestoreConstants.FIELD_MENU_VERSION
import entities.Employee
import extentions.logE
import javax.inject.Inject


class SplashScreenRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) : SSRemoteRepositoryInterface {

    private val defaultMenuVersion = -1L
    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun readCurrentEmployee(email: String, onComplete: (employee: Employee?) -> Unit) {
        employeesCollectionRef.document(email).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val employee = task.result?.toObject<Employee>()
                onComplete(employee)
            } else {
                logE("$this: ${task.exception}")
                onComplete(null)
            }
        }
    }

    override fun readMenuVersion(onComplete: (version: Long) -> Unit) {
        menuDocumentRef.get().addOnSuccessListener {
            val menuVersion = it.data?.get(FIELD_MENU_VERSION)
            if (menuVersion != null) {
                onComplete(menuVersion as Long)
            } else {
                logE("$this: MenuVersion in the remote data source is null")
                onComplete(defaultMenuVersion)
            }
        }.addOnFailureListener {
            logE("$this: ${it.message}")
            onComplete(defaultMenuVersion)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

}