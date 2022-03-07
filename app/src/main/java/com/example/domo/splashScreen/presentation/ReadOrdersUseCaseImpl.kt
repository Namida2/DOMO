package com.example.domo.splashScreen.presentation

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.menu.MenuService
import com.example.core.domain.menu.MenuServiceStates
import com.example.core.domain.menu.MenuServiceSub
import com.example.core.domain.order.Order
import com.example.core.domain.order.OrderItem
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.FirestoreReferences.ordersCollectionRef
import com.example.core.domain.tools.constants.FirestoreConstants.COLLECTION_ORDER_ITEMS
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_GUESTS_COUNT
import com.example.core.domain.tools.extensions.logD
import javax.inject.Inject

class ReadOrdersUseCaseImpl @Inject constructor(
    private val ordersService: OrdersService<OrdersServiceSub>,
) : ReadOrdersUseCase {

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

    override fun readOrders() {
        MenuService.subscribe(subscriber)
    }

    private fun readOrdersInfo() {
        ordersCollectionRef.get().addOnSuccessListener {
            val listOrders = arrayListOf<Order>()
            val lastIndex = it.documents.lastIndex
            it.documents.forEachIndexed { index, docSnapshot ->
                val tableId = docSnapshot.id.toInt()
                val guestsCount = docSnapshot.getLong(FIELD_GUESTS_COUNT) ?: 0
                val order = Order(tableId, guestsCount.toInt())
                listOrders.add(order)
                readOrderItems(docSnapshot.id, order, index == lastIndex, listOrders)
            }
        }.addOnFailureListener {
            logD("something wrong")
        }
    }

    private fun readOrderItems(
        tableId: String,
        order: Order,
        isLastDocument: Boolean,
        listOrders: List<Order>
    ) {
        ordersCollectionRef.document(tableId).collection(COLLECTION_ORDER_ITEMS).get()
            .addOnSuccessListener {
                val orderItems = mutableListOf<OrderItem>()
                it.documents.forEach { document ->
                    document.toObject(OrderItem::class.java)?.let { it1 ->
                        orderItems.add(it1)
                    }
                }
                order.orderItems = orderItems
                if (isLastDocument) {
                    ordersService.addListOfOrders(listOrders)
                }
            }.addOnFailureListener {
                logD("something wrong")
            }
    }
}

interface ReadOrdersUseCase {
    fun readOrders()
}