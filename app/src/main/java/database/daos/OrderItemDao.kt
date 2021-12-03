package database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import entities.order.OrderItem

@Dao
interface OrderItemDao {

    //onConflict
    @Insert
    fun insert(orders: List<OrderItem>)

    @Query("SELECT * FROM order_items")
    fun readAll(): List<OrderItem>

//    @Query("UPDATE order_items set dishId = :newId where dishId = :oldId")
//    fun updateDishId(oldId: Int, newId: Int)

    @Query("delete from order_items where ")
    fun deleteOrderItem

}