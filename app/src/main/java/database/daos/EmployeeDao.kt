package database.daos

import androidx.room.*
import entities.Employee

@Dao
interface EmployeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employees: Employee)
    @Query("DELETE from employee")
    suspend fun deleteAll()
    @Query("select * from employee limit 1")
    suspend fun readCurrentEmployee(): Employee?
}