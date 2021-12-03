package entities.order

//TODO: Add a "one-to-many" relation
data class Order(
    val tableId: Int,
    var guestsCount: Int,
    var orderItems: MutableSet<OrderItem> = mutableSetOf(),
) {
    fun addOrderItem(orderItem: OrderItem): Boolean =
        orderItems.add(orderItem)
}