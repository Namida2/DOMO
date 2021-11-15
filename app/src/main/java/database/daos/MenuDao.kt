package database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import entities.Dish

@Dao
interface MenuDao {

    @Query("SELECT * FROM menu")
    suspend fun readAll(): List<Dish>

    @Insert
    suspend fun insert(dishes: List<Dish>)

}