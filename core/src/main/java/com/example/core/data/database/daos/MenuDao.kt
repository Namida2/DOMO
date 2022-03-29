package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.domain.entities.menu.Dish

@Dao
interface MenuDao {
    @Query("SELECT * FROM menu")
    suspend fun readAll(): List<Dish>
    @Query("DELETE FROM menu")
    suspend fun deleteAll()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dishes: List<Dish>)
}