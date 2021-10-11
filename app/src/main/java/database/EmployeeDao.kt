package database

import androidx.room.Dao
import androidx.room.Insert
import com.example.domo.modeles.Employee

@Dao
interface EmployeeDao {
    @Insert
    fun insert(vararg employees: Employee)
}