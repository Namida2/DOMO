package com.example.featureCurrentOrders.data.repositories

import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.FirestoreConstants.COLLECTION_ORDER_ITEMS
import com.example.core.domain.entities.tools.constants.FirestoreConstants.EMPTY_COMMENTARY
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_ID
import com.example.core.domain.entities.tools.constants.FirestoreReferences.deletedOrderListenerDocumentRef
import com.example.core.domain.entities.tools.constants.FirestoreReferences.ordersCollectionRef
import com.example.core.domain.entities.tools.extensions.addOnSuccessListenerWithDefaultFailureHandler
import com.example.featureCurrentOrders.domain.repositories.OrdersRemoteRepository
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class OrdersRemoteRepositoryImpl @Inject constructor() : OrdersRemoteRepository {

    override fun deleteOrder(orderId: Int, task: SimpleTask) {
        deleteDishes(
            ordersCollectionRef.document(orderId.toString())
                .collection(COLLECTION_ORDER_ITEMS), task
        ) {
            ordersCollectionRef.document(orderId.toString()).delete()
                .addOnSuccessListenerWithDefaultFailureHandler(task) {
                    setOrderIdIntoDeletedOrdersListener(orderId, task)
                }
        }
    }

    private fun deleteDishes(
        dishesCollectionRef: CollectionReference,
        task: SimpleTask,
        onComplete: () -> Unit
    ) {
        dishesCollectionRef.get().addOnSuccessListenerWithDefaultFailureHandler(task) {
            val result = it.result
            val lastDocumentIndex = result.documents.lastIndex.also { index ->
                if (index == -1) {
                    onComplete()
                    return@addOnSuccessListenerWithDefaultFailureHandler
                }
            }
            result.documents.forEachIndexed { index, document ->
                dishesCollectionRef.document(document.id).delete()
                    .addOnSuccessListenerWithDefaultFailureHandler(task) {
                        if (index == lastDocumentIndex) onComplete()
                    }
            }
        }

    }

    private fun setOrderIdIntoDeletedOrdersListener(orderId: Int, task: SimpleTask) {
        resetDeletedOrdersListener(task) {
            deletedOrderListenerDocumentRef.update(
                FIELD_ORDER_ID, orderId
            ).addOnSuccessListenerWithDefaultFailureHandler(task) {
                task.onSuccess(Unit)
            }
        }
    }

    private fun resetDeletedOrdersListener(task: SimpleTask, onComplete: () -> Unit) {
        deletedOrderListenerDocumentRef.update(
            FIELD_ORDER_ID, EMPTY_COMMENTARY
        ).addOnSuccessListenerWithDefaultFailureHandler(task) {
            onComplete()
        }
    }
}


