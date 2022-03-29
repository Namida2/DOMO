package com.example.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.data.database.daos.EmployeeDao
import com.example.core.data.database.daos.MenuDao
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.menu.Dish

@Database(
    entities = [Employee::class, Dish::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
    abstract fun menuDao(): MenuDao
}
