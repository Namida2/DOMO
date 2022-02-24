package com.example.domo.splashScreen.presentation

import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.menu.MenuService
import com.example.waiterCore.domain.menu.MenuServiceStates
import com.example.waiterCore.domain.menu.MenuServiceSub
import com.example.waiterCore.domain.order.Order
import com.example.waiterCore.domain.order.OrderType
import com.example.waiterCore.domain.order.OrdersServiceSub
import com.example.waiterCore.domain.tools.FirestoreReferences.ordersCollectionRef
import com.example.waiterCore.domain.tools.constants.FirestoreConstants.COLLECTION_ORDER_ITEMS
import com.example.waiterCore.domain.tools.constants.FirestoreConstants.FIELD_GUESTS_COUNT
import com.example.waiterCore.domain.tools.extensions.logD
import javax.inject.Inject

class ReadOrdersUseCaseImpl @Inject constructor(
    private val menuService: MenuService,
    private val ordersService: OrdersService<OrdersServiceSub>,
) : ReadOrdersUseCase {

    private val subscriber: MenuServiceSub = object : MenuServiceSub {
        override fun invoke(state: MenuServiceStates) {
            when (state) {
                is MenuServiceStates.MenuExists -> {
                    readOrdersInfo()
                    menuService.unSubscribe(this)
                }
                else -> {}
            }
        }
    }

    override fun readOrders() {
        menuService.subscribe(subscriber)
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
    private fun readOrderItems(tableId: String, order: Order, isLastDocument: Boolean, listOrders: List<Order>) {
        ordersCollectionRef.document(tableId).collection(COLLECTION_ORDER_ITEMS).get()
            .addOnSuccessListener {
                val orderItems = mutableSetOf<OrderType>()
                it.documents.forEach { document ->
                    document.toObject(OrderType::class.java)?.let {
                            it1 -> orderItems.add(it1)
                    }
                }
                order.orderItems = orderItems
                if(isLastDocument) {
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