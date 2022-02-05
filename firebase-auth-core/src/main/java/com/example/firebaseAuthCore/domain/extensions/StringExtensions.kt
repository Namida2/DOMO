package com.example.firebaseAuthCore.domain.extensions

import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.tools.ErrorMessages.defaultErrorMessage
import com.example.waiterCore.domain.tools.ErrorMessages.networkConnectionMessage
import com.example.waiterCore.domain.tools.ErrorMessages.wrongEmailOrPassword
import com.example.waiterCore.domain.tools.FirestoreReferences.employeesCollectionRef
import com.example.waiterCore.domain.tools.TaskWithEmployee
import com.example.waiterCore.domain.tools.extensions.logE
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.ktx.toObject

//Return the employee or null if it doesn't exist
fun String.readEmployeeByEmail (className: String, task: TaskWithEmployee) {
    employeesCollectionRef.document(this).get().addOnSuccessListener { response ->
        val employee = response.toObject<Employee>()
        if(employee == null) {
            task.onError(wrongEmailOrPassword)
            return@addOnSuccessListener
        }
        task.onSuccess(employee)
    }.addOnFailureListener {
        logE("$className, email = $this: ${it.message}")
        if(it is FirebaseNetworkException)
            task.onError(networkConnectionMessage)
        else task.onError(defaultErrorMessage)
    }
}