package database

import androidx.room.Database
import androidx.room.RoomDatabase
import database.daos.EmployeeDao
import database.daos.MenuDao
import entities.Employee
import entities.menu.Dish
import entities.order.OrderItem

@Database(entities = [Employee::class, Dish::class, OrderItem::class],
    version = 1,
    exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
    abstract fun menuDao(): MenuDao
}
