package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.domain.Employee

@Dao
interface EmployeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employees: Employee)

    @Query("DELETE from employee")
    suspend fun deleteAll()

    @Query("select * from employee limit 1")
    suspend fun readCurrentEmployee(): Employee?
}