package entities.order

import androidx.room.Entity
import androidx.room.PrimaryKey
import entities.recyclerView.interfaces.BaseRecyclerViewItem

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey val id: Int,
    val tableId: Int,
    override val dishId: Int,
    override val count: Int,
    override val commentary: String,
    override val isReady: Boolean,
): BaseRecyclerViewItem, BaseOrderItem {
    override fun equals(other: Any?): Boolean =
        if (other !is OrderItem) false
        else dishId == other.dishId && commentary == other.commentary

    override fun hashCode(): Int = "$dishId: $commentary".hashCode()
}

//for fireStore
interface BaseOrderItem {
    val dishId: Int
    val count: Int
    val commentary: String
    val isReady: Boolean
}

