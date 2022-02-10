package com.example.waiterCore.domain.order

import com.example.waiterCore.domain.interfaces.OrdersService
import javax.inject.Inject
import javax.inject.Singleton

typealias OrderServiceSub = (orders: List<Order>) -> Unit
typealias CurrentOrderServiceSub = (orderItems: List<OrderItem>) -> Unit

@Singleton
class OrdersServiceImpl @Inject constructor() :
    OrdersService<@JvmSuppressWildcards OrderServiceSub> {

    private val currentOrderExceptionMessage = "Current order has not been initialized yet."

    override var currentOrder: Order? = null
        get() = field ?: throw IllegalStateException(currentOrderExceptionMessage)

    private var orders = mutableListOf<Order>()
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
        currentOrder?.orderItems?.add(orderItem)
            .also { notifyChangesOfCurrentOrder() }
        //TODO: Add exceptions to com.example.core.domain.constants
            ?: throw IllegalStateException(currentOrderExceptionMessage)

    override fun removeOrder(order: Order) {
        orders.remove(order)
    }

    override fun confirmCurrentOrder() {
        orders.forEachIndexed { index, order ->
            if (order.tableId == currentOrder?.tableId) {
                orders[index] = currentOrder!!
                return
            }
        }
        orders.add(currentOrder!!)
        notifyChanges()
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

}