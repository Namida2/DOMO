package com.example.domo.models.remoteRepository

import com.example.domo.models.remoteRepository.FirestoreReferences.ordersCollectionRef
import com.example.domo.models.remoteRepository.interfaces.OrderMenuDialogRemoteRepositoryInterface
import com.google.firebase.firestore.*
import constants.FirestoreConstants.COLLECTION_ORDER_ITEMS
import constants.FirestoreConstants.DOCUMENT_ORDER_ITEM_DELIMITER
import constants.FirestoreConstants.FIELD_GUESTS_COUNT
import entities.order.BaseOrderItem
import entities.order.Order
import entities.tools.SimpleTask
import extentions.logD
import extentions.logE
import javax.inject.Inject

class OrderMenuDialogRemoteRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) : OrderMenuDialogRemoteRepositoryInterface {

    private val guestCountData = mutableMapOf<String, Int>()

    override fun insertCurrentOrder(order: Order, task: SimpleTask) {
        val orderDocumentRef = ordersCollectionRef
            .document(order.tableId.toString())
        removeAldOrderItems(
            orderDocumentRef.collection(COLLECTION_ORDER_ITEMS),
        ) {
            firestore.runTransaction {
                guestCountData[FIELD_GUESTS_COUNT] = order.guestsCount
                it.set(orderDocumentRef, guestCountData)
                setOrderItems(it, orderDocumentRef, order)
            }.addOnSuccessListener {
                logD("$this: Insertion was successful.")
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
            transaction.set(documentOrderItemRef, it as BaseOrderItem)
        }
    }

    private fun removeAldOrderItems(
        collectionOrderItemsRef: CollectionReference,
        onComplete: () -> Unit
    ) {
        // TODO: // STOPPED 0 //
        collectionOrderItemsRef.get().addOnSuccessListener {
            for (i in 0 .. it.documents.lastIndex) {
                collectionOrderItemsRef.document(it.documents[i].id).delete().addOnSuccessListener { _ ->
                    if(i == it.documents.lastIndex)
                        onComplete.invoke()
                }
            }
        }.addOnFailureListener {
            logD("$this: ${it.message}")
        }
    }
}