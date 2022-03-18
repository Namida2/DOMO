package com.example.core.data.listeners

import com.example.core.domain.tools.NewPermission
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_EMAIL
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_NEW_PERMISSION
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_PERMISSION
import com.example.core.domain.tools.constants.FirestoreReferences.newPermissionListenerDocumentRef
import com.example.core.domain.tools.extensions.logE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

object EmployeePermissionListener {
    private var isFirstData = false

    @OptIn(ExperimentalCoroutinesApi::class)
    val permissionChanges = callbackFlow {
        val subscription = newPermissionListenerDocumentRef.addSnapshotListener { value, error ->
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
                    val data = value.get(FIELD_NEW_PERMISSION) as Map<*, *>
                    val newPermission = data[FIELD_EMAIL]?.let { email ->
                        data[FIELD_PERMISSION]?.let { permission ->
                            NewPermission(
                                email as? String ?: return@addSnapshotListener,
                                permission as? Boolean ?: return@addSnapshotListener
                            )
                        }
                    }
                    trySend(newPermission ?: return@addSnapshotListener)
                }
            }
        }
        awaitClose { subscription.remove() }
    }
}