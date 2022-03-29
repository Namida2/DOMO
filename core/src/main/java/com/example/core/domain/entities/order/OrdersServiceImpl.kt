package com.example.core.domain.entities.order

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.entities.tools.constants.FirestoreConstants.ORDER_ITEM_ID_DELIMITER
import com.example.core.domain.entities.tools.constants.OtherStringConstants.CURRENT_ORDER_NOT_INITIALIZED
import com.example.core.domain.entities.tools.constants.OtherStringConstants.ORDER_ITEM_NOT_FOUNT
import com.example.core.domain.listeners.DeletedOrdersListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias OrdersServiceSub = (orders: List<Order>) -> Unit
typealias CurrentOrderServiceSub = (orderItems: List<OrderItem>) -> Unit

//TODO: Add states to showing the progress bar when the orders is reading
class OrdersServiceImpl @Inject constructor(
    private val deletedOrdersListener: DeletedOrdersListener
) : OrdersService {

    override var currentOrder: Order? = null
        get() = field ?: throw IllegalStateException(CURRENT_ORDER_NOT_INITIALIZED)

    var orders = mutableListOf<Order>()
    private var ordersChanges = MutableSharedFlow<List<Order>>(replay = 1)
    private var currentOrderChanges = MutableSharedFlow<List<OrderItem>>(replay = 1)

    override fun subscribeOnOrdersChanges(): Flow<List<Order>> = ordersChanges
    override fun subscribeOnCurrentOrderChanges(): Flow<List<OrderItem>> = currentOrderChanges


    init {
        CoroutineScope(Main).launch {
            deletedOrdersListener.deletedOrdersInfo.collect { deletedOrderId ->
                orders.find {
                    it.orderId == deletedOrderId
                }.also {
                    if(it == null) return@collect
                    orders.remove(it)
                    ordersChanges.tryEmit(orders)
                }
            }
        }
    }


    override fun addOrderItem(orderItem: OrderItem): Boolean {
        val existingOrderItem = currentOrder!!.orderItems.find {
            it.getOrderIemId() == orderItem.getOrderIemId()
        }
        if (existingOrderItem == null) {
            currentOrder!!.orderItems.add(orderItem)
            currentOrderChanges.tryEmit(currentOrder!!.orderItems)
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
        currentOrderChanges.tryEmit(currentOrder!!.orderItems)
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
        currentOrderChanges.tryEmit(currentOrder!!.orderItems)
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
                ordersChanges.tryEmit(orders)
                return
            }
        }
        orders.add(newOrder)
        ordersChanges.tryEmit(orders)
    }

    override fun addListOfOrders(orders: List<Order>) {
        this.orders.clear()
        this.orders.addAll(orders)
        ordersChanges.tryEmit(orders)
    }

    override fun getOrderById(orderId: Int): Order? =
        orders.find {
            it.orderId == orderId
        }

    override fun changeOrderItemStatus(orderId: Int, orderItemId: String, isReady: Boolean) {
        var newOrder: Order? = null
        orders.find {
            it.orderId == orderId
        }.also {
            newOrder = it?.copy(
                orderItems = it.orderItems.map { orderItem ->
                    if (orderItem.getOrderIemId() == orderItemId)
                        orderItem.copy(isReady = isReady)
                    else orderItem
                }.toMutableList()
            )
        }
        orders.forEachIndexed { index, order ->
            if (order.orderId == orderId) {
                orders[index] = newOrder!!
            }
        }
        ordersChanges.tryEmit(orders)
    }
}

