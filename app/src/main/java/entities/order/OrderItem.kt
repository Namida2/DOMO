package entities.order

data class OrderItem(
    val dishId: Int,
    val count: Int,
    val commentary: String,
) {

}