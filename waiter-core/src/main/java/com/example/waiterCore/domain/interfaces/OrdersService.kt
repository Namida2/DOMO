package com.example.waiterCore.domain.interfaces

import com.example.waiterCore.domain.order.CurrentOrderServiceSub
import com.example.waiterCore.domain.order.Order
import com.example.waiterCore.domain.order.OrderItem

interface OrdersService<Subscriber> : BaseObservable<Subscriber> {
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
}