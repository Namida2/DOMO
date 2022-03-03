package com.example.cookCore.data.repositories

import com.example.core.domain.tools.ErrorMessages.checkNetworkConnectionMessage
import com.example.core.domain.tools.ErrorMessages.defaultErrorMessage
import com.example.core.domain.tools.FirestoreReferences.fireStore
import com.example.core.domain.tools.FirestoreReferences.orderItemsStateListenerDocumentRef
import com.example.core.domain.tools.FirestoreReferences.ordersCollectionRef
import com.example.core.domain.tools.SimpleTask
import com.example.core.domain.tools.constants.FirestoreConstants.COLLECTION_ORDER_ITEMS
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_ID
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_IS_READY
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_ITEM_ID
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Transaction
import javax.inject.Inject

class OrderItemsRemoteRepositoryImpl @Inject constructor() : OrderItemsRemoteRepository {
    override fun setOrderItemAsReady(orderId: Int, orderItemId: String, task: SimpleTask) {
        val orderItemDocumentRef = ordersCollectionRef.document(orderId.toString())
            .collection(COLLECTION_ORDER_ITEMS).document(orderItemId)
        fireStore.runTransaction {
            it.update(orderItemDocumentRef, FIELD_ORDER_IS_READY, true, SetOptions.merge())
            updateOrderItemsStateListener(orderId, orderItemId, it)
        }.addOnSuccessListener {
            task.onSuccess(Unit)
        }.addOnFailureListener {
            if(it is FirebaseNetworkException)
                task.onError(checkNetworkConnectionMessage)
            else task.onError(defaultErrorMessage)
        }
    }

    private fun updateOrderItemsStateListener(
        orderId: Int,
        orderItemId: String,
        transaction: Transaction
    ) {
        val orderItemInfo = mapOf<String, Any>(
            FIELD_ORDER_ID to orderId,
            FIELD_ORDER_ITEM_ID to orderItemId
        )
        transaction.update(orderItemsStateListenerDocumentRef, orderItemInfo)
    }
}

interface OrderItemsRemoteRepository {
    fun setOrderItemAsReady(orderId: Int, orderItemId: String, task: SimpleTask)
}