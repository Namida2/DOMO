package com.example.core.domain.tools.extensions

import com.example.core.domain.Employee
import com.example.core.domain.tools.constants.ErrorMessages.wrongEmailOrPassword
import com.example.core.domain.tools.constants.FirestoreReferences.employeesCollectionRef
import com.google.firebase.firestore.ktx.toObject

fun isEmptyField(vararg strings: String): Boolean {
    strings.forEach {
        if (it.isEmpty()) return true
        if (it.replace(" ", "").isEmpty()) return true
    }
    return false
}

fun String.isValidEmail(): Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.readEmployeeByEmail (className: String, task: com.example.core.domain.tools.TaskWithEmployee) {
    employeesCollectionRef.document(this).get().addOnSuccessListener { response ->
        val employee = response.toObject<Employee>()
        if(employee == null) {
            task.onError(wrongEmailOrPassword)
            return@addOnSuccessListener
        }
        task.onSuccess(employee)
    }.addOnFailureListener {
        logE("$className, email = $this: ${it.message}")
        task.onError(it.getExceptionMessage())
    }
}
