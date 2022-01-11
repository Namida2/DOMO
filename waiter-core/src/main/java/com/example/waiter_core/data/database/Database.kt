package com.example.waiter_core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.waiter_core.data.database.daos.EmployeeDao
import com.example.waiter_core.data.database.daos.MenuDao
import com.example.waiter_core.domain.Employee
import com.example.waiter_core.domain.menu.Dish
import com.example.waiter_core.domain.order.OrderItem

@Database(entities = [Employee::class, Dish::class, OrderItem::class],
    version = 1,
    exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
    abstract fun menuDao(): MenuDao
}
