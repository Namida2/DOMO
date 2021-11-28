package entities.order

//TODO: Add ids for dishes in remote datasource
data class OrderItem(
    val dishId: Int,
    val count: Int,
    val commentary: String,
) {

}