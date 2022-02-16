package com.example.waiterCore.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.waiterCore.data.database.daos.EmployeeDao
import com.example.waiterCore.data.database.daos.MenuDao
import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.menu.Dish
import com.example.waiterCore.domain.order.OrderItem

@Database(entities = [Employee::class, Dish::class],
    version = 1,
    exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
    abstract fun menuDao(): MenuDao
}
