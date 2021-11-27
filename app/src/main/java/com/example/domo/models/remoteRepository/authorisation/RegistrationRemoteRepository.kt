package com.example.domo.models.remoteRepository.authorisation

import com.example.domo.R
import com.example.domo.views.activities.log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import constants.FirestoreConstants.COLLECTION_EMPLOYEES
import constants.FirestoreConstants.COLLECTION_RESTAURANTS
import constants.FirestoreConstants.DOCUMENT_DOMO
import entities.Employee
import entities.ErrorMessage
import entities.tools.TaskWithEmployee
import javax.inject.Inject

class RegistrationRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
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
        task: TaskWithEmployee,
    ) {
        auth.fetchSignInMethodsForEmail(employee.email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.signInMethods!!.isEmpty()) {
                        log("${this}: New email")
                        createNewEmployee(employee, task)
                    } else {
                        log("${this}: Email already exits")
                        task.onError(
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
        task: TaskWithEmployee,
    ) {
        auth.createUserWithEmailAndPassword(employee.email, employee.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    addEmployeeToCollection(employee, task)
                } else {
                    log("${this}: ${it.exception.toString()}")
                    task.onSuccess(employee)
                }
            }
    }

    private fun addEmployeeToCollection(
        employee: Employee,
        task: TaskWithEmployee,
    ) {
        fireStore.runTransaction {
            it.set(employeesCollectionRef.document(employee.email), employee)
        }.addOnCompleteListener {
            if (it.isSuccessful)
                task.onSuccess(employee)
            else {
                log("${this}: ${it.exception.toString()}")
                auth.currentUser?.delete()
                task.onError(
                    ErrorMessage(
                        defaultExceptionTitle,
                        defaultExceptionMessage
                    )
                )
            }
        }
    }


}

