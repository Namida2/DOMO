package com.example.featureSplashScreen.data

import com.example.core.domain.menu.MenuService
import com.example.core.domain.menu.MenuServiceStates
import com.example.core.domain.menu.MenuServiceSub
import com.example.core.domain.order.Order
import com.example.core.domain.order.OrderItem
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.SimpleTask
import com.example.core.domain.tools.Task
import com.example.core.domain.tools.constants.FirestoreConstants
import com.example.core.domain.tools.constants.FirestoreReferences
import com.example.core.domain.tools.extensions.getExceptionMessage
import com.example.core.domain.tools.extensions.logD
import com.example.featureSplashScreen.domain.repositories.OrdersRemoteRepository
import javax.inject.Inject

typealias TaskWithOrders = Task<List<Order>, Unit>
class OrdersRemoteRepositoryImpl @Inject constructor(): OrdersRemoteRepository {

    private lateinit var task: TaskWithOrders
    private val subscriber: MenuServiceSub = object : MenuServiceSub {
        override fun invoke(state: MenuServiceStates) {
            when (state) {
                is MenuServiceStates.MenuExists -> {
                    readOrdersInfo()
                    MenuService.unSubscribe(this)
                } else -> {}
            }
        }
    }

    override fun readOrders(task: TaskWithOrders) {
        this.task = task
        MenuService.subscribe(subscriber)
    }

    private fun readOrdersInfo() {
        FirestoreReferences.ordersCollectionRef.get().addOnSuccessListener {
            val listOrders = arrayListOf<Order>()
            val lastIndex = it.documents.lastIndex
            it.documents.forEachIndexed { index, docSnapshot ->
                val tableId = docSnapshot.id.toInt()
                val guestsCount = docSnapshot.getLong(FirestoreConstants.FIELD_GUESTS_COUNT) ?: 0
                val order = Order(tableId, guestsCount.toInt())
                listOrders.add(order)
                readOrderItems(docSnapshot.id, order, index == lastIndex, listOrders)
            }
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }

    private fun readOrderItems(
        tableId: String,
        order: Order,
        isLastDocument: Boolean,
        listOrders: List<Order>
    ) {
        FirestoreReferences.ordersCollectionRef.document(tableId).collection(FirestoreConstants.COLLECTION_ORDER_ITEMS).get()
            .addOnSuccessListener {
                val orderItems = mutableListOf<OrderItem>()
                it.documents.forEach { document ->
                    document.toObject(OrderItem::class.java)?.let { it1 ->
                        orderItems.add(it1)
                    }
                }
                order.orderItems = orderItems
                if (isLastDocument) {
                    task.onSuccess(listOrders)
                }
            }.addOnFailureListener {
                task.onError(it.getExceptionMessage())
            }
    }
}