package com.example.featureEmployees.data.repositories

import com.example.core.domain.Employee
import com.example.core.domain.tools.FirestoreReferences.employeesCollectionRef
import com.example.core.domain.tools.SimpleTask
import com.example.core.domain.tools.extensions.getExceptionMessage
import com.example.featureEmployees.domain.EmployeesService
import com.example.featureEmployees.domain.repositories.EmployeesRepository
import javax.inject.Inject

class EmployeesRepositoryImpl @Inject constructor(): EmployeesRepository {

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
}
