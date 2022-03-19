package com.example.domo.models.remoteRepository.authorisation

import com.example.core.domain.entities.Employee
import com.example.core.domain.tools.constants.FirestoreConstants.COLLECTION_EMPLOYEES
import com.example.core.domain.tools.constants.FirestoreConstants.COLLECTION_RESTAURANTS
import com.example.core.domain.tools.constants.FirestoreConstants.DOCUMENT_DOMO
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import com.example.domo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class RegistrationRemoteRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) {
    private val emailAlreadyExistsTitle = R.string.emailAlreadyExitsTitle
    private val emailAlreadyExistsMessage = R.string.emailAlreadyExistsMessage
    private val defaultExceptionTitle = R.string.defaultTitle
    private val defaultExceptionMessage = R.string.defaultTitle
    private val employeesCollectionRef: CollectionReference =
        fireStore.collection(COLLECTION_RESTAURANTS).document(DOCUMENT_DOMO)
            .collection(COLLECTION_EMPLOYEES)

    fun registration(
        employee: Employee,
        task: com.example.core.domain.tools.TaskWithEmployee,
    ) {
        auth.fetchSignInMethodsForEmail(employee.email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.signInMethods!!.isEmpty()) {
                        logD("${this}: New email")
                        createNewEmployee(employee, task)
                    } else {
                        logE("${this}: Email already exits")
                        task.onError(
                            com.example.core.domain.tools.ErrorMessage(
                                emailAlreadyExistsTitle,
                                emailAlreadyExistsMessage
                            )
                        )
                    }
                } else {
                    logE("${this}: ${it.exception.toString()}")
                }
            }
    }

    private fun createNewEmployee(
        employee: Employee,
        task: com.example.core.domain.tools.TaskWithEmployee,
    ) {
        auth.createUserWithEmailAndPassword(employee.email, employee.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    addEmployeeToCollection(employee, task)
                } else {
                    logE("${this}: ${it.exception.toString()}")
                    task.onSuccess(employee)
                }
            }
    }

    private fun addEmployeeToCollection(
        employee: Employee,
        task: com.example.core.domain.tools.TaskWithEmployee,
    ) {
        fireStore.runTransaction {
            it.set(employeesCollectionRef.document(employee.email), employee)
        }.addOnCompleteListener {
            if (it.isSuccessful)
                task.onSuccess(employee)
            else {
                logE("${this}: ${it.exception.toString()}")
                auth.currentUser?.delete()
                task.onError(
                    com.example.core.domain.tools.ErrorMessage(
                        defaultExceptionTitle,
                        defaultExceptionMessage
                    )
                )
            }
        }
    }


}

