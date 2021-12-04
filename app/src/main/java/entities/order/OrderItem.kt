package entities.order

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import entities.recyclerView.interfaces.BaseRecyclerViewItem

@Entity(tableName = "order_items", primaryKeys = ["tableId", "dishId", "commentary"])
data class OrderItem (
    val tableId: Int,
    override val dishId: Int,
    override val count: Int,
    override val commentary: String,
    override val isReady: Boolean = false,
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

