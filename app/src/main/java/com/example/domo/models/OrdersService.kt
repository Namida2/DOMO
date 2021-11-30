package com.example.domo.models

import entities.interfaces.OrderServiceInterface
import entities.order.Order
import entities.order.OrderItem
import javax.inject.Inject

typealias OrderServiceSub = (orders: List<Order>) -> Unit

class OrdersService @Inject constructor() :
    OrderServiceInterface<@kotlin.jvm.JvmSuppressWildcards OrderServiceSub> {

    override var currentOrder: Order? = null
    private var orders = mutableSetOf<Order>()
    private var subscribers = mutableSetOf<OrderServiceSub>()

    override fun subscribe(subscriber: OrderServiceSub) {
        subscribers.add(subscriber)
    }

    override fun unSubscribe(subscriber: OrderServiceSub) {
        subscribers.remove(subscriber)
    }

    override fun notifyChanges() {
        subscribers.forEach {
            it.invoke(orders.toList())
        }
    }

    override fun addOrderItem(orderItem: OrderItem): Boolean =
        currentOrder?.addOrderItem(orderItem) //TODO: Add exceptions to constants
            ?: throw IllegalStateException("Current order has not been initialised.")

    override fun removeOrder(order: Order) {
        orders.remove(order)
    }
//
//    override fun getOrder(tableId: Int): Order =
//        orders.find {
//            it.tableId == tableId
//        } ?: Order(tableId).also {
//            orders.add(it)
//        }

    override fun confirmCurrentOrder() {
        orders.add(currentOrder!!)
        notifyChanges()
    }

    override fun initCurrentOrder(tableId: Int, guestCount: Int) {
        if(currentOrder != null) return
        currentOrder = Order(tableId, guestCount)
    }

}