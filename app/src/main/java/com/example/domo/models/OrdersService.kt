package com.example.domo.models

import entities.interfaces.BaseObservable
import entities.interfaces.OrderServiceInterface
import entities.order.Order
import javax.inject.Inject
import javax.inject.Singleton

typealias OrderServiceSub = (orders: List<Order>) -> Unit
//TODO: Add entities and realisation // STOPPED //
@Singleton
class OrdersService @Inject constructor(): OrderServiceInterface<OrderServiceSub> {
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

    override fun addOrder(order: Order) {
        orders.add(order)
        var a = mutableMapOf<Order, Int>()
    }

    override fun removeOrder(tableId: Int) {
        TODO("Not yet implemented")
    }
}