package entities.order

data class Order(val tableId: Int, var orderItems: List<OrderItem>)