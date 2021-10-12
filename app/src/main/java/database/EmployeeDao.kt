package database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import entities.Employee

@Dao
interface EmployeeDao {
    @Insert
    suspend fun insert(employees: Employee)
    @Query("select * from employee")
    suspend fun readCurrentEmployee(): Employee
}