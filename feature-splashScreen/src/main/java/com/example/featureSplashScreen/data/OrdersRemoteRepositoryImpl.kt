package com.example.featureSplashScreen.data

import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.menu.MenuServiceStates
import com.example.core.domain.entities.menu.MenuServiceSub
import com.example.core.domain.entities.order.Order
import com.example.core.domain.entities.order.OrderItem
import com.example.core.domain.entities.tools.Task
import com.example.core.domain.entities.tools.constants.FirestoreConstants
import com.example.core.domain.entities.tools.constants.FirestoreReferences
import com.example.core.domain.entities.tools.extensions.getExceptionMessage
import com.example.featureSplashScreen.domain.repositories.OrdersRemoteRepository
import javax.inject.Inject

typealias TaskWithOrders = Task<List<Order>, Unit>

class OrdersRemoteRepositoryImpl @Inject constructor() : OrdersRemoteRepository {

    private lateinit var task: TaskWithOrders
    private var collectionOrdersSize = 0
    private var readOrdersCount = 0
    private val subscriber: MenuServiceSub = object : MenuServiceSub {
        override fun invoke(state: MenuServiceStates) {
            when (state) {
                is MenuServiceStates.MenuExists -> {
                    readOrdersInfo()
                    MenuService.unSubscribe(this)
                }
                else -> {}
            }
        }
    }

    override fun readOrders(task: TaskWithOrders) {
        this.task = task
        readOrdersCount = 0
        MenuService.subscribe(subscriber)
    }

    private fun readOrdersInfo() {
        FirestoreReferences.ordersCollectionRef.get().addOnSuccessListener {
            val listOrders = arrayListOf<Order>()
            collectionOrdersSize = it.documents.size
            if(collectionOrdersSize == 0) {
                task.onSuccess(listOrders)
                return@addOnSuccessListener
            }
            it.documents.forEach { docSnapshot ->
                val tableId = docSnapshot.id.toInt()
                val guestsCount =
                    docSnapshot.getLong(FirestoreConstants.FIELD_GUESTS_COUNT)?.toInt() ?: 0
                val order = Order(tableId, guestsCount)
                listOrders.add(order)
                readOrderItems(docSnapshot.id, order, listOrders)
            }
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }

    private fun readOrderItems(
        tableId: String,
        order: Order,
        listOrders: List<Order>
    ) {
        FirestoreReferences.ordersCollectionRef.document(tableId)
            .collection(FirestoreConstants.COLLECTION_ORDER_ITEMS).get()
            .addOnSuccessListener {
                ++readOrdersCount
                val orderItems = mutableListOf<OrderItem>()
                it.documents.forEach { document ->
                    document.toObject(OrderItem::class.java)?.let { it1 ->
                        orderItems.add(it1)
                    }
                }
                order.orderItems = orderItems
                if (collectionOrdersSize == readOrdersCount) {
                    task.onSuccess(listOrders)
                }
            }.addOnFailureListener {
                task.onError(it.getExceptionMessage())
            }
    }
}