package entities.order

data class OrderItem(
    val dishId: Int,
    val count: Int,
    val commentary: String,
) {
    override fun equals(other: Any?): Boolean =
        if (other !is OrderItem) false
        else dishId == other.dishId && commentary == other.commentary

    override fun hashCode(): Int = "$dishId: $commentary".hashCode()
}