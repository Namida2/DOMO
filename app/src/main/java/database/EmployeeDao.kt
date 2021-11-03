package database

import androidx.room.*
import entities.Employee

@Dao
interface EmployeeDao {
    @Insert
    suspend fun insert(employees: Employee)
    @Query("DELETE from employee")
    suspend fun deleteAll()
    @Query("select * from employee")
    suspend fun readCurrentEmployee(): Employee?
}