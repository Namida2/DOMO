package com.example.core.domain.order

import com.example.core.domain.interfaces.OrdersService
import javax.inject.Inject

typealias OrdersServiceSub = (orders: List<Order>) -> Unit
typealias CurrentOrderServiceSub = (orderItems: List<OrderItem>) -> Unit

//TODO: Add states to showing the progress bar when the orders is reading
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
            it.orderId == tableId
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
            if (order.orderId == newOrder.orderId) {
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
        notifyChanges()
    }

    override fun getOrderById(orderId: Int): Order? =
        orders.find {
            it.orderId == orderId
        }

    //TODO: Notify about data changes
    override fun changeOrderItemStatus(orderId: Int, orderItemId: String) {
        orders.find {
            it.orderId == orderId
        }?.orderItems?.find {
            it.getOrderIemId() == orderItemId
        }?.isReady = true
    }


}