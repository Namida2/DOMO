package com.example.domo.models.remoteRepository

import com.example.domo.R
import com.example.domo.views.log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import constants.FirestoreConstants.COLLECTION_EMPLOYEES
import constants.FirestoreConstants.COLLECTION_RESTAURANTS
import constants.FirestoreConstants.DOCUMENT_DOMO
import entities.Employee
import entities.ErrorMessage
import javax.inject.Inject

class RegistrationRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) {
    private val emailAlreadyExistsTitle = R.string.emailAlreadyExitTitle
    private val emailAlreadyExistsMessage = R.string.emailAlreadyExistMessage
    private val defaultExceptionTitle = R.string.defaultTitle
    private val defaultExceptionMessage = R.string.defaultTitle
    private val employeesCollectionRef: CollectionReference =
        fireStore.collection(COLLECTION_RESTAURANTS).document(DOCUMENT_DOMO)
            .collection(COLLECTION_EMPLOYEES)

    fun registration(
        employee: Employee,
        onSuccess: () -> Unit,
        onError: (errorMessage: ErrorMessage) -> Unit
    ) {
        auth.fetchSignInMethodsForEmail(employee.email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.signInMethods!!.isEmpty()) {
                        log("${this}: New email")
                        createNewEmployee(employee, onSuccess, onError)
                    } else {
                        log("${this}: Email already exits")
                        onError(
                            ErrorMessage(
                                emailAlreadyExistsTitle,
                                emailAlreadyExistsMessage
                            )
                        )
                    }
                } else {
                    log("${this}: ${it.exception.toString()}")
                }
            }

    }

    private fun createNewEmployee(
        employee: Employee,
        onSuccess: () -> Unit,
        onError: (errorMessage: ErrorMessage) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(employee.email, employee.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    addEmployeeToCollection(employee, onSuccess, onError)
                } else {
                    log("${this}: ${it.exception.toString()}")
                    onSuccess()
                }
            }
    }

    private fun addEmployeeToCollection(
        employee: Employee,
        onSuccess: () -> Unit,
        onError: (errorMessage: ErrorMessage) -> Unit
    ) {
        fireStore.runTransaction {
            it.set(employeesCollectionRef.document(employee.email), employee)
        }.addOnCompleteListener {
            if (it.isSuccessful)
                onSuccess()
            else {
                log("${this}: ${it.exception.toString()}")
                auth.currentUser?.delete()
                onError(
                    ErrorMessage(
                        defaultExceptionTitle,
                        defaultExceptionMessage
                    )
                )
            }
        }
    }


}

