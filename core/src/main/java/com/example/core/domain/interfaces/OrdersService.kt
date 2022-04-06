package com.example.core.domain.interfaces

import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.order.Order
import com.example.core.domain.entities.order.OrderItem
import kotlinx.coroutines.flow.Flow

interface OrdersService {
    var currentOrder: Order?
    fun subscribeOnOrdersChanges(): Flow<List<Order>>
    fun subscribeOnCurrentOrderChanges(): Flow<List<OrderItem>>

    fun addOrderItem(orderItem: OrderItem): Boolean
    fun updateOrderItem(orderItem: OrderItem, aldCommentary: String): Boolean
    fun deleteOrder(order: Order)
    fun confirmCurrentOrder()
    fun initCurrentOrder(tableId: Int, guestCount: Int)
    fun changeGuestsCount(newCount: Int)
    fun getCurrentOrderItems(): Set<OrderItem>
    fun deleteFromCurrentOrder(dish: Dish, commentary: String): Boolean

    fun addOrder(newOrder: Order)
    fun addListOfOrders(orders: List<Order>)
    fun getOrderById(orderId: Int): Order?
    fun changeOrderItemStatus(orderId: Int, orderItemId: String, isReady: Boolean)
}