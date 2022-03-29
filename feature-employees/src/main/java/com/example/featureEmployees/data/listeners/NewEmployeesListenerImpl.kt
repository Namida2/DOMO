package com.example.featureEmployees.data.listeners

import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.core.domain.entities.tools.constants.FirestoreConstants.EMPTY_COMMENTARY
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_EMAIL
import com.example.core.domain.entities.tools.constants.FirestoreReferences.newEmployeeListenerDocumentRef
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.domain.entities.tools.extensions.readEmployeeByEmail
import com.example.featureEmployees.domain.listeners.NewEmployeesListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NewEmployeesListenerImpl @Inject constructor() : NewEmployeesListener {
    private var isFirstData = false

    @OptIn(ExperimentalCoroutinesApi::class)
    override val newEmployeesFlow: Flow<Employee> = callbackFlow {
        val subscription = newEmployeeListenerDocumentRef.addSnapshotListener { value, error ->
            when {
                error != null -> {
                    logE("$this: $error")
                    return@addSnapshotListener
                }
                value != null && value.exists() && value.data != null -> {
                    if (!isFirstData) {
                        isFirstData = true
                        return@addSnapshotListener
                    }
                    value.getString(FIELD_EMAIL).also {
                        if (it == EMPTY_COMMENTARY) return@addSnapshotListener
                    }?.readEmployeeByEmail(this.toString(), object : TaskWithEmployee {
                        override fun onSuccess(result: Employee) {
                            trySend(result)
                        }
                        override fun onError(message: ErrorMessage?) {
                            logE("$this: $message")
                        }
                    })
                }
            }
        }
        awaitClose {
            subscription.remove()
        }
    }

}