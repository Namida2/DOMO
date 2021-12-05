package com.example.domo.models.remoteRepository

import com.example.domo.models.remoteRepository.FirestoreReferences.ordersCollectionRef
import com.example.domo.models.remoteRepository.interfaces.OrderMenuDialogRemoteRepositoryInterface
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
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
        firestore.runTransaction {
            val orderDocumentRef = ordersCollectionRef
                .document(order.tableId.toString())
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


}