package com.example.waiterCore.domain.order

import com.example.waiterCore.domain.interfaces.OrdersService
import javax.inject.Inject
import javax.inject.Singleton

typealias OrdersServiceSub = (orders: List<Order>) -> Unit
typealias CurrentOrderServiceSub = (orderItems: List<OrderItem>) -> Unit

class OrdersServiceImpl @Inject constructor() :
    OrdersService<@JvmSuppressWildcards OrdersServiceSub> {

    private val currentOrderExceptionMessage = "Current order has not been initialized."

    override var currentOrder: Order? = null
        get() = field ?: throw IllegalStateException(currentOrderExceptionMessage)

    var orders = mutableListOf<Order>()
    private var subscribers = mutableSetOf<OrdersServiceSub>()
    override var currentOrderSubscribers: MutableSet<CurrentOrderServiceSub> = mutableSetOf()

    override fun notifyChangesOfCurrentOrder() {
        currentOrderSubscribers.forEach {
            it.invoke(currentOrder?.orderItems?.toList()!!)
        }
    }

    override fun subscribe(subscriber: OrdersServiceSub) {
        subscribers.add(subscriber)
        subscriber.invoke(orders)
    }

    override fun unSubscribe(subscriber: OrdersServiceSub) {
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
        currentOrder!!.orderItems.add(orderItem)
            .also { notifyChangesOfCurrentOrder() }

    override fun removeOrder(order: Order) {
        orders.remove(order)
    }

    override fun confirmCurrentOrder() {
        addOrder(currentOrder!!)
    }

    override fun initCurrentOrder(tableId: Int, guestCount: Int) {
        val result: Order? = orders.find {
            it.tableId == tableId
        }
        currentOrder = result?.copy() ?: Order(tableId, guestCount)

        val newOrderItems = mutableSetOf<OrderItem>()
        result?.orderItems?.forEach {
            newOrderItems.add(it.copy())
        }
        currentOrder?.orderItems = newOrderItems
    }

    override fun changeGuestsCount(newCount: Int) {
        currentOrder?.also {
            it.guestsCount = newCount
        } ?: throw IllegalStateException(currentOrderExceptionMessage)
    }

    override fun getCurrentOrderItems(): Set<OrderItem> =
        currentOrder?.orderItems?.toSet()!!

    override fun addOrder(newOrder: Order) {
        orders.forEachIndexed { index, order ->
            if (order.tableId == newOrder.tableId) {
                orders[index] = newOrder
                return
            }
        }
        orders.add(newOrder)
        notifyChanges()
    }

    override fun addListOfOrders(orders: List<Order>) {
        this.orders.clear()
        this.orders.addAll(orders)
    }

}