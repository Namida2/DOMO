package entities.order

data class Order(val tableId: Int, val guestsCount: Int, var orderItems: MutableSet<OrderItem> = mutableSetOf()) {
    fun addOrderItem(orderItem: OrderItem): Boolean =
        orderItems.add(orderItem)
}