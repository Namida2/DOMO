package com.example.waiterMain.data

import com.example.waiterCore.domain.order.Order
import com.example.waiterCore.domain.order.OrderItem
import com.example.waiterCore.domain.tools.ErrorMessages
import com.example.waiterCore.domain.tools.FirestoreReferences.ordersCollectionRef
import com.example.waiterCore.domain.tools.TaskWithOrder
import com.example.waiterCore.domain.tools.constants.FirestoreConstants
import com.example.waiterCore.domain.tools.extensions.logE
import com.google.firebase.FirebaseNetworkException
import javax.inject.Inject

class OrdersRemoteRepositoryImpl @Inject constructor() : OrdersRemoteRepository {

    override fun readOrderItems(order: Order, task: TaskWithOrder) {
        val tableId = order.tableId.toString()
        ordersCollectionRef.document(tableId).collection(FirestoreConstants.COLLECTION_ORDER_ITEMS)
            .get().addOnSuccessListener {
                val orderItems = mutableSetOf<OrderItem>()
                it.documents.forEach { document ->
                    document.toObject(OrderItem::class.java)?.let { it1 ->
                        orderItems.add(it1)
                    }
                }
                order.orderItems = orderItems
                task.onSuccess(order)
            }.addOnFailureListener {
                logE("$this: $it")
                if(it is FirebaseNetworkException)
                    task.onError(ErrorMessages.networkConnectionMessage)
                else task.onError(ErrorMessages.defaultErrorMessage)
            }
    }

}
interface OrdersRemoteRepository {
    fun readOrderItems(order: Order, task: TaskWithOrder)
}

