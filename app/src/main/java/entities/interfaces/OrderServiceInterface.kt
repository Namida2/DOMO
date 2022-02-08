package entities.interfaces

import com.example.domo.models.CurrentOrderServiceSub
import com.example.waiterCore.domain.interfaces.BaseObservable
import com.example.waiterCore.domain.order.Order
import com.example.waiterCore.domain.order.OrderItem

interface OrderServiceInterface<Subscriber> : BaseObservable<Subscriber> {
    var currentOrder: Order?
    var currentOrderSubscribers: MutableSet<CurrentOrderServiceSub>
    fun notifyChangesOfCurrentOrder()
    fun subscribeToCurrentOrderChangers(subscriber: CurrentOrderServiceSub)
    fun unSubscribeToCurrentOrderChangers(subscriber: CurrentOrderServiceSub)
    fun addOrderItem(orderItem: OrderItem): Boolean
    fun removeOrder(order: Order)
    fun confirmCurrentOrder()
    fun initCurrentOrder(tableId: Int, guestCount: Int)
    fun changeGuestsCount(newCount: Int)
}