package database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.domo.modeles.Employee
import dagger.Module

@Database(entities = [Employee::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}
