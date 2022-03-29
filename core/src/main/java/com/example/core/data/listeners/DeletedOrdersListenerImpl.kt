package com.example.core.data.listeners

import com.example.core.domain.entities.tools.constants.FirestoreConstants
import com.example.core.domain.entities.tools.constants.FirestoreReferences.deletedOrderListenerDocumentRef
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.domain.listeners.DeletedOrdersListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DeletedOrdersListenerImpl @Inject constructor() : DeletedOrdersListener {
    private var isFirstData = true

    @OptIn(ExperimentalCoroutinesApi::class)
    override val deletedOrdersInfo = callbackFlow {
        val subscription = deletedOrderListenerDocumentRef.addSnapshotListener { value, error ->
            when {
                error != null -> {
                    logE("$this: $error")
                    return@addSnapshotListener
                }
                value != null && value.exists() && value.data != null -> {
                    if (isFirstData) {
                        isFirstData = false
                        return@addSnapshotListener
                    }
                    val deletedOrderId = (value.get(FirestoreConstants.FIELD_ORDER_ID) as? Long)?.toInt()
                    trySend(deletedOrderId ?: return@addSnapshotListener)
                }
            }
        }
        awaitClose {
            subscription.remove()
        }
    }
}