package com.example.featureEmployees.data.repositories

import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.FirestoreConstants.EMPTY_COMMENTARY
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_EMAIL
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_NEW_PERMISSION
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_PERMISSION
import com.example.core.domain.entities.tools.constants.FirestoreReferences.employeesCollectionRef
import com.example.core.domain.entities.tools.constants.FirestoreReferences.fireStore
import com.example.core.domain.entities.tools.constants.FirestoreReferences.newPermissionListenerDocumentRef
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.entities.tools.constants.Messages.employeeMaybeDeletedByAnotherAdminMessage
import com.example.core.domain.entities.tools.extensions.getExceptionMessage
import com.example.featureEmployees.domain.repositories.EmployeesRepository
import com.example.featureEmployees.domain.services.EmployeesService
import com.google.firebase.firestore.Transaction
import javax.inject.Inject

class EmployeesRepositoryImpl @Inject constructor(
    private val employeesService: EmployeesService
) : EmployeesRepository {

    override fun readAllEmployees(task: SimpleTask) {
        employeesCollectionRef.get().addOnSuccessListener {
            val employees = mutableListOf<Employee>()
            it.documents.forEach { employeeDoc ->
                employeeDoc.toObject(Employee::class.java)
                    ?.let { it -> employees.add(it) }
            }
            employeesService.setNewEmployeesList(employees)
            task.onSuccess(Unit)
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }

    override fun setPermissionForEmployee(
        employee: Employee,
        permission: Boolean,
        task: SimpleTask
    ) {
        setEmptyNewPermission(task) {
            fireStore.runTransaction {
                setPermissionsToDocuments(it, employee, permission)
            }.addOnSuccessListener {
                task.onSuccess(Unit)
            }.addOnFailureListener {
                task.onError(it.getExceptionMessage())
            }
        }
    }

    override fun deleteEmployee(employee: Employee, task: SimpleTask) {
        setEmptyNewPermission(task) {
            fireStore.runTransaction {
                setPermissionsToDocuments(it, employee, false)
                it.delete(employeesCollectionRef.document(employee.email))
            }.addOnSuccessListener {
                task.onSuccess(Unit)
            }.addOnFailureListener {
                val message = it.getExceptionMessage()
                if(message == defaultErrorMessage)
                    task.onError(employeeMaybeDeletedByAnotherAdminMessage)
                else task.onError(message)
            }
        }
    }

    private fun setEmptyNewPermission(
        task: SimpleTask, onComplete: () -> Unit
    ) {
        newPermissionListenerDocumentRef.update(
            mapOf(
                FIELD_NEW_PERMISSION to mapOf(
                    FIELD_EMAIL to EMPTY_COMMENTARY,
                    FIELD_PERMISSION to EMPTY_COMMENTARY
                )
            )
        ).addOnSuccessListener {
            onComplete.invoke()
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }

    private fun setPermissionsToDocuments(
        transaction: Transaction,
        employee: Employee,
        permission: Boolean
    ) {
        transaction.update(
            employeesCollectionRef.document(employee.email),
            FIELD_PERMISSION,
            permission
        )
        transaction.update(
            newPermissionListenerDocumentRef, mapOf(
                FIELD_NEW_PERMISSION to mapOf(
                    FIELD_EMAIL to employee.email,
                    FIELD_PERMISSION to permission
                )
            )
        )
    }
}
