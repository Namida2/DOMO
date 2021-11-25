package database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import entities.menu.Dish

@Dao
interface MenuDao {

    @Query("SELECT * FROM menu")
    suspend fun readAll(): List<Dish>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dishes: List<Dish>)

}