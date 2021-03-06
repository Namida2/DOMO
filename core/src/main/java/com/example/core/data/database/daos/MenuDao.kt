package com.example.core.data.database.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.core.domain.entities.menu.Dish

@Dao
abstract class MenuDao {
    @Query("SELECT * FROM menu")
    abstract suspend fun readAll(): List<Dish>
    @Query("DELETE FROM menu")
    abstract suspend fun deleteAll()
    @Insert(onConflict = REPLACE)
    abstract suspend fun insert(dishes: List<Dish>): List<Long>

    @Transaction
    open suspend fun insertWithDeleteAldMenu(dishes: List<Dish>) {
        deleteAll()
        insert(dishes)
    }
}