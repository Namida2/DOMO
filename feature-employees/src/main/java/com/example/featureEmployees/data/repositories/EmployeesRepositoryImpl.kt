package com.example.featureEmployees.data.repositories

import com.example.core.domain.Employee
import com.example.core.domain.tools.FirestoreReferences.employeesCollectionRef
import com.example.core.domain.tools.FirestoreReferences.fireStore
import com.example.core.domain.tools.FirestoreReferences.newPermissionDocumentRef
import com.example.core.domain.tools.SimpleTask
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_EMAIL
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_NEW_PERMISSION
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_PERMISSION
import com.example.core.domain.tools.extensions.getExceptionMessage
import com.example.featureEmployees.domain.EmployeesService
import com.example.featureEmployees.domain.repositories.EmployeesRepository
import javax.inject.Inject

class EmployeesRepositoryImpl @Inject constructor() : EmployeesRepository {

    override fun readAllEmployees(task: SimpleTask) {
        employeesCollectionRef.get().addOnSuccessListener {
            val employees = mutableListOf<Employee>()
            it.documents.forEach { employeeDoc ->
                employeeDoc.toObject(Employee::class.java)
                    ?.let { it -> employees.add(it) }
            }
            EmployeesService.setNewEmployeesList(employees)
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
        fireStore.runTransaction {
            it.update(
                employeesCollectionRef.document(employee.email),
                FIELD_PERMISSION,
                permission
            )
            it.update(
                newPermissionDocumentRef, mapOf(
                    FIELD_NEW_PERMISSION to mapOf(
                        FIELD_EMAIL to employee.email,
                        FIELD_PERMISSION to permission
                    )
                )
            )
        }.addOnSuccessListener {
            task.onSuccess(Unit)
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }

    override fun deleteEmployee(employee: Employee, task: SimpleTask) {
        employeesCollectionRef.document(employee.email).delete().addOnSuccessListener {
            task.onSuccess(Unit)
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }


}
