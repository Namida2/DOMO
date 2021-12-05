package entities.order

import androidx.room.*

//TODO: Add a "one-to-many" relation
data class Order (
    @Embedded val orderInfo: OrderInfo,
    @Relation(
        parentColumn = "tableId",
        entityColumn = "tableId"
    )
    var orderItems: MutableSet<OrderItem> = mutableSetOf(),
) {
    @Ignore
    var tableId: Int = orderInfo.tableId

    @Ignore
    var guestsCount: Int = orderInfo.tableId

    constructor(_tableId: Int, _guestsCount: Int) : this(OrderInfo(_tableId, _guestsCount)) {
        tableId = _tableId
        guestsCount = _guestsCount
    }

    fun addOrderItem(orderItem: OrderItem): Boolean =
        orderItems.add(orderItem)

}

@Entity(tableName = "order_info")
data class OrderInfo(
    @PrimaryKey val tableId: Int,
    var guestsCount: Int,
)

