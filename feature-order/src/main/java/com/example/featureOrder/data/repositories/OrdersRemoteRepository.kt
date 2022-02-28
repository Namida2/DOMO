package com.example.featureOrder.data.repositories

import com.example.core.domain.order.Order
import com.example.core.domain.tools.FirestoreReferences.newOrdersListenerDocumentRef
import com.example.core.domain.tools.FirestoreReferences.ordersCollectionRef
import com.example.core.domain.tools.constants.FirestoreConstants.COLLECTION_ORDER_ITEMS
import com.example.core.domain.tools.constants.FirestoreConstants.DOCUMENT_ORDER_ITEM_DELIMITER
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_GUESTS_COUNT
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_ID
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_INFO
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import javax.inject.Inject

class OrdersRemoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : OrdersRemoteRepository {

    private val guestCountData = mutableMapOf<String, Int>()

    override fun insertCurrentOrder(order: Order, task: com.example.core.domain.tools.SimpleTask) {
        val orderDocumentRef = ordersCollectionRef
            .document(order.orderId.toString())
        removeAldOrderItems(
            orderDocumentRef.collection(COLLECTION_ORDER_ITEMS),
        ) {
            firestore.runTransaction {
                guestCountData[FIELD_GUESTS_COUNT] = order.guestsCount
                it.set(orderDocumentRef, guestCountData)
                setOrderItems(it, orderDocumentRef, order)
            }.addOnSuccessListener {
                logD("$this: Insertion was successful.")
                insertNewOrderDateToListener(order)
                task.onSuccess(Unit)
            }.addOnFailureListener {
                it.message?.let { it1 -> logE(it1) }
                task.onError()
            }
        }
    }

    private fun setOrderItems(
        transaction: Transaction,
        orderDocumentRef: DocumentReference,
        order: Order,
    ) {
        order.orderItems.forEach {
            val orderItemDocumentName =
                "${it.dishId}$DOCUMENT_ORDER_ITEM_DELIMITER${it.commentary}"
            val documentOrderItemRef =
                orderDocumentRef.collection(COLLECTION_ORDER_ITEMS)
                    .document(orderItemDocumentName)
            transaction.set(documentOrderItemRef, it)
        }
    }

    private fun removeAldOrderItems(
        collectionOrderItemsRef: CollectionReference,
        onComplete: () -> Unit,
    ) {
        // TODO: To find out if there is a better solution
        collectionOrderItemsRef.get().addOnSuccessListener {
            for (i in 0..it.documents.lastIndex) {
                collectionOrderItemsRef.document(it.documents[i].id).delete()
                    .addOnSuccessListener { _ ->
                        logD("Removed index: $i")
                        if (i == it.documents.lastIndex) {
                            logD(i.toString())
                            onComplete.invoke()
                        }
                    }
            }
            if (it.documents.lastIndex == -1)
                onComplete.invoke()
        }.addOnFailureListener {
            onComplete.invoke()
            logE("$this: ${it.message}")
        }
    }

    override fun insertNewOrderDateToListener(order: Order) {
        val (tableId, guestsCount) = order
        val newOrderData = mapOf<String, Any>(
            FIELD_ORDER_INFO to mapOf<String, Any>(
                FIELD_ORDER_ID to tableId,
                FIELD_GUESTS_COUNT to guestsCount
            )
        )
        newOrdersListenerDocumentRef.set(newOrderData).addOnFailureListener {
            logE("$this: ${it.message}")
        }
    }
}

interface OrdersRemoteRepository {
    fun insertCurrentOrder(order: Order, task: com.example.core.domain.tools.SimpleTask)
    fun insertNewOrderDateToListener(order: Order)
}