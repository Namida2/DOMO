package com.example.core.data.repositorues

import com.example.core.domain.entities.order.Order
import com.example.core.domain.entities.order.OrderItem
import com.example.core.domain.entities.tools.constants.Messages
import com.example.core.domain.entities.tools.constants.FirestoreReferences.ordersCollectionRef
import com.example.core.domain.entities.tools.TaskWithOrder
import com.example.core.domain.entities.tools.constants.FirestoreConstants
import com.example.core.domain.entities.tools.extensions.logE
import com.google.firebase.FirebaseNetworkException
import javax.inject.Inject

class OrdersRemoteRepositoryImpl @Inject constructor(): OrdersRemoteRepository {

    override fun readOrderItems(order: Order, task: TaskWithOrder) {
        val tableId = order.orderId.toString()
        ordersCollectionRef.document(tableId).collection(FirestoreConstants.COLLECTION_ORDER_ITEMS)
            .get().addOnSuccessListener {
                val orderItems = mutableListOf<OrderItem>()
                it.documents.forEach { document ->
                    document.toObject(OrderItem::class.java)?.let { it1 ->
                        orderItems.add(it1)
                    }
                }
                order.orderItems = orderItems
                task.onSuccess(order)
            }.addOnFailureListener {
                logE("$this: $it")
                if (it is FirebaseNetworkException)
                    task.onError(Messages.checkNetworkConnectionMessage)
                else task.onError(Messages.defaultErrorMessage)
            }
    }

}

interface OrdersRemoteRepository {
    fun readOrderItems(order: Order, task: TaskWithOrder)
}

