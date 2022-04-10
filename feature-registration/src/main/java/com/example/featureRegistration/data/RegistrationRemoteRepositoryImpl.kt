package com.example.featureRegistration.data

import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.core.domain.entities.tools.constants.EmployeePosts
import com.example.core.domain.entities.tools.constants.FirestoreConstants
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.entities.tools.constants.Messages.emailAlreadyExistsMessage
import com.example.core.domain.entities.tools.constants.FirestoreConstants.EMPTY_COMMENTARY
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_EMAIL
import com.example.core.domain.entities.tools.constants.FirestoreReferences.employeesCollectionRef
import com.example.core.domain.entities.tools.constants.FirestoreReferences.fireStore
import com.example.core.domain.entities.tools.constants.FirestoreReferences.newEmployeeListenerDocumentRef
import com.example.core.domain.entities.tools.constants.Messages
import com.example.core.domain.entities.tools.extensions.addOnSuccessListenerWithDefaultFailureHandler
import com.example.core.domain.entities.tools.extensions.getExceptionMessage
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.entities.tools.extensions.logE
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

interface RegistrationRemoteRepository {
    fun registration(
        employee: Employee,
        task: TaskWithEmployee,
    )
    fun logInAsAdministrator(employee: Employee,
                             task: TaskWithEmployee,)
}

class RegistrationRemoteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : RegistrationRemoteRepository {

    override fun registration(
        employee: Employee,
        task: TaskWithEmployee,
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
                    task.onError(it.exception?.getExceptionMessage())
                }
            }
    }

    override fun logInAsAdministrator(employee: Employee, task: TaskWithEmployee) {
        auth.signInWithEmailAndPassword(employee.email, employee.password).addOnCompleteListener {
            if (it.isSuccessful) {
                setPermissionForAdministrator(employee, task)
            } else {
                logE("$this: ${it.exception}")
                task.onError(Messages.wrongEmailOrPassword)
            }
        }
    }

    private fun setPermissionForAdministrator(employee: Employee, task: TaskWithEmployee) {
        employeesCollectionRef.document(employee.email).update(
            FirestoreConstants.FIELD_PERMISSION, true
        ).addOnSuccessListenerWithDefaultFailureHandler(task) { task.onSuccess(employee) }
    }

    private fun createNewEmployee(
        employee: Employee,
        task: TaskWithEmployee,
    ) {
        auth.createUserWithEmailAndPassword(employee.email, employee.password)
            .addOnSuccessListener {
                addEmployeeToCollection(employee, task)
            }.addOnFailureListener {
                logE("${this}: $it")
                task.onError(it.getExceptionMessage())
            }
    }

    private fun addEmployeeToCollection(
        employee: Employee,
        task: TaskWithEmployee,
    ) {
        setEmptyEmailToNewEmployeeListener(task) {
            fireStore.runTransaction {
                it.set(employeesCollectionRef.document(employee.email), employee)
                it.update(newEmployeeListenerDocumentRef, FIELD_EMAIL, employee.email)
            }.addOnSuccessListener {
                task.onSuccess(employee)
            }.addOnFailureListener {
                logE("${this}: $it")
                auth.currentUser?.delete()
                task.onError(it.getExceptionMessage())
            }
        }
    }

    private fun setEmptyEmailToNewEmployeeListener(task: TaskWithEmployee, onComplete: () -> Unit) {
        newEmployeeListenerDocumentRef.update(
            FIELD_EMAIL, EMPTY_COMMENTARY
        ).addOnSuccessListener {
            onComplete.invoke()
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }
}

