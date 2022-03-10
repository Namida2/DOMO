package com.example.cookCore.data.repositories

import com.example.core.domain.tools.FirestoreReferences.fireStore
import com.example.core.domain.tools.FirestoreReferences.orderItemsStateListenerDocumentRef
import com.example.core.domain.tools.FirestoreReferences.ordersCollectionRef
import com.example.core.domain.tools.SimpleTask
import com.example.core.domain.tools.constants.FirestoreConstants.COLLECTION_ORDER_ITEMS
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_ID
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_IS_READY
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_ITEM_ID
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_ITEM_INFO
import com.example.core.domain.tools.extensions.getExceptionMessage
import com.google.firebase.firestore.Transaction
import javax.inject.Inject

class OrderItemsRemoteRepositoryImpl @Inject constructor() : OrderItemsRemoteRepository {
    override fun setOrderItemAsReady(orderId: Int, orderItemId: String, task: SimpleTask) {
        val orderItemDocumentRef = ordersCollectionRef.document(orderId.toString())
            .collection(COLLECTION_ORDER_ITEMS).document(orderItemId)
        resetOrderItemState(task) {
            fireStore.runTransaction {
                it.update(orderItemDocumentRef, FIELD_ORDER_IS_READY, true)
                updateOrderItemsStateListener(orderId, orderItemId, it)
            }.addOnSuccessListener {
                task.onSuccess(Unit)
            }.addOnFailureListener {
                task.onError(it.getExceptionMessage())
            }
        }
    }

    private fun resetOrderItemState(task: SimpleTask, onSuccess: () -> Unit) {
        orderItemsStateListenerDocumentRef.set(
            mapOf<String, Any>(
                FIELD_ORDER_ITEM_INFO to mapOf<String, Any>(
                    FIELD_ORDER_ID to "",
                    FIELD_ORDER_ITEM_ID to ""
                )
            )
        ).addOnSuccessListener {
            onSuccess.invoke()
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }


    private fun updateOrderItemsStateListener(
        orderId: Int,
        orderItemId: String,
        transaction: Transaction
    ) {
        val orderItemInfo = mapOf<String, Any>(
            FIELD_ORDER_ITEM_INFO to mapOf<String, Any>(
                FIELD_ORDER_ID to orderId,
                FIELD_ORDER_ITEM_ID to orderItemId
            )
        )
        transaction.update(orderItemsStateListenerDocumentRef, orderItemInfo)
    }

}

interface OrderItemsRemoteRepository {
    fun setOrderItemAsReady(orderId: Int, orderItemId: String, task: SimpleTask)
}