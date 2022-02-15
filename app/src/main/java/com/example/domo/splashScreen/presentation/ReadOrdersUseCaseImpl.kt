package com.example.domo.splashScreen.presentation

import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.menu.MenuService
import com.example.waiterCore.domain.menu.MenuServiceStates
import com.example.waiterCore.domain.menu.MenuServiceSub
import com.example.waiterCore.domain.order.Order
import com.example.waiterCore.domain.order.OrdersServiceSub
import com.example.waiterCore.domain.tools.FirestoreReferences.ordersCollectionRef
import com.example.waiterCore.domain.tools.constants.FirestoreConstants.COLLECTION_ORDER_ITEMS
import com.example.waiterCore.domain.tools.constants.FirestoreConstants.FIELD_GUESTS_COUNT
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class ReadOrdersUseCaseImpl @Inject constructor(
    private val menuService: MenuService,
    private val ordersService: OrdersService<OrdersServiceSub>,
    private val firestore: FirebaseFirestore
): ReadOrdersUseCase {

    private val subsciber: MenuServiceSub = object: MenuServiceSub {
        override fun invoke(state: MenuServiceStates) {
            when(state) {
                is MenuServiceStates.MenuExists -> {

                    //unsubscribe
                }
                else -> {}
            }
        }

    }
    override fun readOrders() {
        menuService.subscribe (subsciber)
        TODO("Not yet implemented")
    }

    private fun readOrdersInfo() {
        ordersCollectionRef.get().addOnSuccessListener {
            val listOrders = arrayListOf<Order>()
            val lastIndex = it.documents.lastIndex
            it.documents.forEachIndexed { index, docSnapshot ->
                val tableId = docSnapshot.id.toInt()
                val guestsCount = docSnapshot.getLong(FIELD_GUESTS_COUNT) ?: 0
                val order = Order(tableId, guestsCount)
                listOrders.add(order)
                readOrderItems(docSnapshot.id, index == lastIndex)
            }
        }.addOnFailureListener {
            //onError
        }
    }
    //TODO: Read orders //STOPPED//

    private fun readOrderItems(tableId: String, isLastDocument: Boolean) {
        ordersCollectionRef.document(tableId).collection(COLLECTION_ORDER_ITEMS).get().addOnSuccessListener {

        }.addOnCanceledListener {

        }
    }
}

interface ReadOrdersUseCase {
    fun readOrders()
}