package com.example.domo.registration.data

import com.example.core.domain.Employee
import com.example.core.domain.tools.ErrorMessages.defaultErrorMessage
import com.example.core.domain.tools.ErrorMessages.emailAlreadyExistsMessage
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

interface RegistrationRemoteRepository {
    fun registration(
        employee: Employee,
        task: com.example.core.domain.tools.TaskWithEmployee,
    )
}

class RegistrationRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) : RegistrationRemoteRepository {

    private val employeesCollectionRef: CollectionReference =
        fireStore.collection(com.example.core.domain.tools.constants.FirestoreConstants.COLLECTION_RESTAURANTS)
            .document(com.example.core.domain.tools.constants.FirestoreConstants.DOCUMENT_DOMO)
            .collection(com.example.core.domain.tools.constants.FirestoreConstants.COLLECTION_EMPLOYEES)

    override fun registration(
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
                        task.onError(emailAlreadyExistsMessage)
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
                task.onError(defaultErrorMessage)
            }
        }
    }
}
