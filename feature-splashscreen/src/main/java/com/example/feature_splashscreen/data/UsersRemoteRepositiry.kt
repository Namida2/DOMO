package com.example.feature_splashscreen.data

import com.example.waiter_core.domain.Employee
import com.example.waiter_core.domain.tools.FirestoreReferences.employeesCollectionRef
import com.example.waiter_core.domain.tools.extentions.logE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.toObject
import javax.inject.Inject

class UsersRemoteRepositiryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : UsersRemoteRepositiry {

    private val defaultMenuVersion = -1L
    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun readCurrentEmployee(email: String, onComplete: (employee: Employee?) -> Unit) {
        employeesCollectionRef.document(email).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val employee = task.result?.toObject<Employee>()
                onComplete(employee)
            } else {
                logE("$this: ${task.exception}")
                onComplete(null)
            }
        }
    }


    override fun signOut() {
        auth.signOut()
    }

}

interface UsersRemoteRepositiry {
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
    fun readCurrentEmployee(email: String, onComplete: (employee: Employee?) -> Unit)
}
