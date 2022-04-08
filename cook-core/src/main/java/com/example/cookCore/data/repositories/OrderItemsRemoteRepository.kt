package com.example.cookCore.data.repositories

import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.FirestoreConstants.COLLECTION_ORDER_ITEMS
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_ID
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_IS_READY
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_ITEM_ID
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_ITEM_INFO
import com.example.core.domain.entities.tools.constants.FirestoreReferences.fireStore
import com.example.core.domain.entities.tools.constants.FirestoreReferences.orderItemsStateListenerDocumentRef
import com.example.core.domain.entities.tools.constants.FirestoreReferences.ordersCollectionRef
import com.example.core.domain.entities.tools.extensions.getExceptionMessage
import com.google.firebase.firestore.Transaction
import javax.inject.Inject

class OrderItemsRemoteRepositoryImpl @Inject constructor() : OrderItemsRemoteRepository {

    override fun setOrderItemAsReady(
        orderId: Int,
        orderItemId: String,
        isReady: Boolean,
        task: SimpleTask
    ) {
        val orderItemDocumentRef = ordersCollectionRef.document(orderId.toString())
            .collection(COLLECTION_ORDER_ITEMS).document(orderItemId)
        resetOrderItemState(task) {
            fireStore.runTransaction {
                it.update(orderItemDocumentRef, FIELD_IS_READY, isReady)
                updateOrderItemsStateListener(orderId, orderItemId, isReady, it)
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
                    FIELD_ORDER_ITEM_ID to "",
                    FIELD_IS_READY to ""
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
        isReady: Boolean,
        transaction: Transaction
    ) {
        val orderItemInfo = mapOf<String, Any>(
            FIELD_ORDER_ITEM_INFO to mapOf<String, Any>(
                FIELD_ORDER_ID to orderId,
                FIELD_ORDER_ITEM_ID to orderItemId,
                FIELD_IS_READY to isReady
            )
        )
        transaction.update(orderItemsStateListenerDocumentRef, orderItemInfo)
    }

}

interface OrderItemsRemoteRepository {
    fun setOrderItemAsReady(orderId: Int, orderItemId: String, isReady: Boolean, task: SimpleTask)
}