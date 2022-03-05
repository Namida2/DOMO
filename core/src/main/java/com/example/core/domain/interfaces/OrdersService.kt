package com.example.core.domain.interfaces

import com.example.core.domain.order.CurrentOrderServiceSub
import com.example.core.domain.order.Order
import com.example.core.domain.order.OrderItem

interface OrdersService<Subscriber>: BaseObservable<Subscriber> {
    var currentOrder: Order?
    var currentOrderSubscribers: MutableSet<CurrentOrderServiceSub>
    fun notifyChangesOfCurrentOrder()
    fun subscribeToCurrentOrderChangers(subscriber: CurrentOrderServiceSub)
    fun unSubscribeToCurrentOrderChangers(subscriber: CurrentOrderServiceSub)

    fun addOrderItem(orderItem: OrderItem): Boolean
    fun removeOrder(order: Order)
    fun confirmCurrentOrder()
    fun initCurrentOrder(tableId: Int, guestCount: Int)
    fun changeGuestsCount(newCount: Int)
    fun getCurrentOrderItems(): Set<OrderItem>

    fun addOrder(order: Order)
    fun addListOfOrders(orders: List<Order>)
    fun getOrderById(orderId: Int): Order?
    fun changeOrderItemStatus(orderId: Int, orderItemId: String)
}