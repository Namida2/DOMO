package com.example.firebase_auth_core.domain.extensions

import com.example.waiter_core.domain.Employee
import com.example.waiter_core.domain.tools.ErrorMessages.networkConnectionMessage
import com.example.waiter_core.domain.tools.FirestoreReferences.employeesCollectionRef
import com.example.waiter_core.domain.tools.TaskWithEmployee
import com.example.waiter_core.domain.tools.extensions.logE
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.ktx.toObject

//Return the employee or null if it doesn't exist
fun String.readEmployeeByEmail (className: String, task: TaskWithEmployee) {
    employeesCollectionRef.document(this).get().addOnSuccessListener { response ->
        val employee = response.toObject<Employee>()
        if(employee == null) {
            task.onError()
            return@addOnSuccessListener
        }
        task.onSuccess(employee)
    }.addOnFailureListener {
        logE("$className, email = $this: ${it.message}")
        if(it is FirebaseNetworkException)
            task.onError(networkConnectionMessage)
        else task.onError()
    }
}