package entities.interfaces

import entities.order.Order
import entities.order.OrderItem

interface OrderServiceInterface<Subscriber> : BaseObservable<Subscriber>{
    var currentOrder: Order?
    fun addOrderItem(orderItem: OrderItem): Boolean
    fun removeOrder(order: Order)
    fun confirmCurrentOrder()
    fun initCurrentOrder(tableId: Int, guestCount: Int)
}