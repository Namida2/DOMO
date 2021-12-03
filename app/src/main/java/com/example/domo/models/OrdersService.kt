package com.example.domo.models

import entities.interfaces.OrderServiceInterface
import entities.order.Order
import entities.order.OrderItem
import javax.inject.Inject

typealias OrderServiceSub = (orders: List<Order>) -> Unit
typealias CurrentOrderServiceSub = (orderItems: List<OrderItem>) -> Unit

class OrdersService @Inject constructor() :
    OrderServiceInterface<@kotlin.jvm.JvmSuppressWildcards OrderServiceSub> {

    override var currentOrder: Order? = null
    get() = field ?: throw IllegalStateException("Order has not been initialized yet.")

    private var orders = mutableSetOf<Order>()
    private var subscribers = mutableSetOf<OrderServiceSub>()
    override var currentOrderSubscribers: MutableSet<CurrentOrderServiceSub> = mutableSetOf()

    override fun notifyChangesOfCurrentOrder() {
        currentOrderSubscribers.forEach {
            it.invoke(currentOrder?.orderItems?.toList()!!)
        }
    }

    override fun subscribe(subscriber: OrderServiceSub) {
        subscribers.add(subscriber)
    }

    override fun unSubscribe(subscriber: OrderServiceSub) {
        subscribers.remove(subscriber)
    }

    override fun subscribeToCurrentOrderChangers(subscriber: CurrentOrderServiceSub) {
        currentOrderSubscribers.add(subscriber)
    }

    override fun unSubscribeToCurrentOrderChangers(subscriber: CurrentOrderServiceSub) {
        currentOrderSubscribers.remove(subscriber)
    }

    override fun notifyChanges() {
        subscribers.forEach {
            it.invoke(orders.toList())
        }
    }

    override fun addOrderItem(orderItem: OrderItem): Boolean =
        currentOrder?.addOrderItem(orderItem).also { notifyChangesOfCurrentOrder() } //TODO: Add exceptions to constants
            ?: throw IllegalStateException("Current order has not been initialised.")

    override fun removeOrder(order: Order) {
        orders.remove(order)
    }

    override fun confirmCurrentOrder() {
        orders.add(currentOrder!!)
        notifyChanges()
    }

    override fun initCurrentOrder(tableId: Int, guestCount: Int) {
        val result: Order? = orders.find { it.tableId == tableId }
        currentOrder = result?.also {
            it.guestsCount = guestCount
        } ?: Order(tableId, guestCount)
    }

    override fun changeGuestsCount(newCount: Int) {
        TODO("Not yet implemented")
    }

}