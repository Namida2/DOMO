package entities.order

import androidx.room.*

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
        set(value) {
            field = value
            orderInfo.tableId = value
        }

    @Ignore
    var guestsCount: Int = orderInfo.guestsCount
    set(value) {
        field = value
        orderInfo.guestsCount = value
    }

    constructor(_tableId: Int, _guestsCount: Int) : this(OrderInfo(_tableId, _guestsCount)) {
        tableId = _tableId
        guestsCount = _guestsCount
    }

    fun addOrderItem(orderItem: OrderItem): Boolean =
        orderItems.add(orderItem)

}

@Entity(tableName = "order_info")
data class OrderInfo(
    @PrimaryKey var tableId: Int,
    var guestsCount: Int,
)

