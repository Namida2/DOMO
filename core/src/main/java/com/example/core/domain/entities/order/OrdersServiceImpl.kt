package com.example.core.domain.entities.order

import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.tools.constants.FirestoreConstants.ORDER_ITEM_ID_DELIMITER
import com.example.core.domain.entities.tools.constants.OtherStringConstants.CURRENT_ORDER_NOT_INITIALIZED
import com.example.core.domain.entities.tools.constants.OtherStringConstants.ORDER_ITEM_NOT_FOUNT
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.listeners.DeletedOrdersListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO: Add states to showing the progress bar when the orders is reading
class OrdersServiceImpl @Inject constructor(
    private val deletedOrdersListener: DeletedOrdersListener
) : OrdersService {

    override var currentOrder: Order? = null

    var orders = mutableListOf<Order>()
    private var deletedOrderJob: Job? = null
    private var ordersChanges = MutableSharedFlow<List<Order>>(replay = 1)
    private var currentOrderChanges = MutableSharedFlow<List<OrderItem>>(replay = 1)

    override fun subscribeOnOrdersChanges(): Flow<List<Order>> = ordersChanges
    override fun subscribeOnCurrentOrderChanges(): Flow<List<OrderItem>> = currentOrderChanges


    private fun listenDeletedOrderListenerChanges() {
        deletedOrderJob?.cancel()
        deletedOrderJob = CoroutineScope(Main).launch {
            deletedOrdersListener.deletedOrdersInfo.collect { deletedOrderId ->
                orders.find {
                    it.orderId == deletedOrderId
                }.also {
                    if (it == null) return@collect
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
        var anotherExistingOrderItem: OrderItem? = null
        val aldOrderItemId = orderItem.dishId.toString() + ORDER_ITEM_ID_DELIMITER + aldCommentary
        currentOrder!!.orderItems.find {
            it.getOrderIemId() == aldOrderItemId
        } ?: throw IllegalArgumentException(ORDER_ITEM_NOT_FOUNT)
        if (orderItem.commentary != aldCommentary) {
            anotherExistingOrderItem = currentOrder!!.orderItems.find {
                it.getOrderIemId() == orderItem.getOrderIemId()
            }
            if (anotherExistingOrderItem != null) return false
        }

        currentOrder!!.orderItems = currentOrder!!.orderItems.map {
            if (it.getOrderIemId() == aldOrderItemId) orderItem
            else it
        }.toMutableList()
        currentOrderChanges.tryEmit(currentOrder!!.orderItems)
        return true
    }

    override fun deleteOrder(order: Order) {
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

    override fun deleteFromCurrentOrder(dish: Dish, commentary: String): Boolean {
        val orderItem = currentOrder!!.orderItems.find {
            it.dishId == dish.id && it.commentary == commentary
        } ?: return false
        currentOrder!!.orderItems.remove(orderItem)
        currentOrderChanges.tryEmit(currentOrder!!.orderItems)
        return true
    }

    override fun addOrder(newOrder: Order) {
        if (currentOrder?.orderId == newOrder.orderId) {
            currentOrder = newOrder
            currentOrder?.orderItems?.let { currentOrderChanges.tryEmit(it) }
        }
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

    //TODO: This should always be called
    override fun addListOfOrders(orders: List<Order>) {
        this.orders.clear()
        this.orders.addAll(orders)
        ordersChanges.tryEmit(orders)
        listenDeletedOrderListenerChanges()
    }

    override fun getOrderById(orderId: Int): Order? =
        orders.find {
            it.orderId == orderId
        }

    override fun changeOrderItemStatus(orderId: Int, orderItemId: String, isReady: Boolean) {
        orders.indexOfFirst {
            it.orderId == orderId
        }.also {
            if(it == -1) return@also
            orders[it] = orders[it].copy(
                orderItems = copyOrderItemsWithNewOrderItemStatus(
                    orders[it].orderItems,
                    orderItemId,
                    isReady
                )
            )
            changeOrderItemStatusInCurrentOrder(orderId, orderItemId, isReady)
            ordersChanges.tryEmit(orders)
        }
    }

    private fun changeOrderItemStatusInCurrentOrder(orderId: Int, orderItemId: String, isReady: Boolean) {
        if(currentOrder?.orderId == orderId) {
            currentOrder = currentOrder?.copy(
                orderItems = copyOrderItemsWithNewOrderItemStatus(
                    currentOrder!!.orderItems,
                    orderItemId,
                    isReady
                )
            )
            currentOrderChanges.tryEmit(currentOrder!!.orderItems)
        }
    }

    private fun copyOrderItemsWithNewOrderItemStatus(
        orderItems: List<OrderItem>,
        orderItemId: String,
        isReady: Boolean
    ): MutableList<OrderItem> = orderItems.map { orderItem ->
        if (orderItem.getOrderIemId() == orderItemId)
            orderItem.copy(isReady = isReady)
        else orderItem
    }.toMutableList()
}

