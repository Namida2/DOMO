package entities.interfaces

import entities.order.Order

interface OrderServiceInterface<Subscriber> : BaseObservable<Subscriber>{
    fun addOrder(order: Order)
    fun removeOrder(tableId: Int)
}