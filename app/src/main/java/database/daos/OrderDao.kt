package database.daos

import androidx.room.*
import entities.order.Order
import entities.order.OrderInfo
import entities.order.OrderItem

@Dao
interface OrderDao {

    @Transaction
    suspend fun insert(order: Order) {
        insertOrderInfo(order.orderInfo)
        insertOrderItems(order.orderItems.toList())
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderInfo(order: OrderInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(orderItems: List<OrderItem>)

    @Transaction
    @Query("select * from order_info where tableId = :tableId")
    suspend fun readOrder(tableId: Int): Order

    @Transaction
    @Query("select * from order_info")
    suspend fun readAllOrders(): List<Order>

    @Transaction
    suspend fun deleteOrder(tableId: Int) {
        deleteOrderInfo(tableId)
        deleteOrderItems(tableId)
    }

    @Query("delete from order_info where tableId = :tableId")
    suspend fun deleteOrderInfo(tableId: Int)

    @Query("delete from order_items where tableId = :tableId")
    suspend fun deleteOrderItems(tableId: Int)
}