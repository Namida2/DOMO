package com.example.core.domain.order

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.tools.constants.FirestoreConstants.ORDER_ITEM_ID_DELIMITER
import com.example.core.domain.tools.constants.OtherStringConstants.CURRENT_ORDER_NOT_INITIALIZED
import com.example.core.domain.tools.constants.OtherStringConstants.ORDER_ITEM_NOT_FOUNT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject

typealias OrdersServiceSub = (orders: List<Order>) -> Unit
typealias CurrentOrderServiceSub = (orderItems: List<OrderItem>) -> Unit

//TODO: Add states to showing the progress bar when the orders is reading
class OrdersServiceImpl @Inject constructor():
    OrdersService<@JvmSuppressWildcards OrdersServiceSub> {

    override var currentOrder: Order? = null
        get() = field ?: throw IllegalStateException(CURRENT_ORDER_NOT_INITIALIZED)

    var orders = mutableListOf<Order>()
    private var ordersSubscribers = MutableSharedFlow<List<Order>>(replay = 1)
    private var currentOrderSubscribers = MutableSharedFlow<List<OrderItem>>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun subscribeOnOrdersChanges(): Flow<List<Order>> = ordersSubscribers

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun subscribeOnCurrentOrderChanges(): Flow<List<OrderItem>> = currentOrderSubscribers

    override fun addOrderItem(orderItem: OrderItem): Boolean {
        val existingOrderItem = currentOrder!!.orderItems.find {
            it.getOrderIemId() == orderItem.getOrderIemId()
        }
        if (existingOrderItem == null) {
            currentOrder!!.orderItems.add(orderItem)
            currentOrderSubscribers.tryEmit(currentOrder!!.orderItems)
            return true
        }
        return false
    }

    override fun updateOrderItem(orderItem: OrderItem, aldCommentary: String): Boolean {
        val aldOrderItemId = orderItem.dishId.toString() + ORDER_ITEM_ID_DELIMITER + aldCommentary
        currentOrder!!.orderItems.find {
            it.getOrderIemId() == aldOrderItemId
        } ?: throw IllegalArgumentException(ORDER_ITEM_NOT_FOUNT)
        val anotherExistingOrderItem = currentOrder!!.orderItems.find {
            it.getOrderIemId() == orderItem.getOrderIemId()
        }
        if (anotherExistingOrderItem != null) return false

        currentOrder!!.orderItems = currentOrder!!.orderItems.map {
            if (it.getOrderIemId() == aldOrderItemId) orderItem
            else it
        }.toMutableList()
        currentOrderSubscribers.tryEmit(currentOrder!!.orderItems)
        return true
    }

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

        val newOrderItems = mutableListOf<OrderItem>()
        result?.orderItems?.forEach {
            newOrderItems.add(it.copy())
        }
        currentOrder?.orderItems = newOrderItems
        currentOrderSubscribers.tryEmit(currentOrder!!.orderItems)
    }

    override fun changeGuestsCount(newCount: Int) {
        currentOrder?.also {
            it.guestsCount = newCount
        } ?: throw IllegalStateException(CURRENT_ORDER_NOT_INITIALIZED)
    }

    override fun getCurrentOrderItems(): Set<OrderItem> =
        currentOrder?.orderItems?.toSet()!!

    override fun addOrder(newOrder: Order) {
        orders.forEachIndexed { index, order ->
            if (order.orderId == newOrder.orderId) {
                orders[index] = newOrder
                ordersSubscribers.tryEmit(orders)
                return
            }
        }
        orders.add(newOrder)
        ordersSubscribers.tryEmit(orders)
    }

    override fun addListOfOrders(orders: List<Order>) {
        this.orders.clear()
        this.orders.addAll(orders)
        ordersSubscribers.tryEmit(orders)
    }

    override fun getOrderById(orderId: Int): Order? =
        orders.find {
            it.orderId == orderId
        }

    override fun changeOrderItemStatus(orderId: Int, orderItemId: String) {
        var newOrder: Order? = null
        orders.find {
            it.orderId == orderId
        }.also {
            newOrder = it?.copy(
                orderItems = it.orderItems.map { orderItem ->
                    if (orderItem.getOrderIemId() == orderItemId)
                        orderItem.copy(isReady = true)
                    else orderItem
                }.toMutableList()
            )
        }
        orders.forEachIndexed { index, order ->
            if (order.orderId == orderId) {
                orders[index] = newOrder!!
            }
        }
        ordersSubscribers.tryEmit(orders)
    }
}

