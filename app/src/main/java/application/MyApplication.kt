package application

import android.app.Application
import android.content.Context
import androidx.room.Room
import database.Database
import database.EmployeeDao
import di.AppComponent
import di.DaggerAppComponent
import entities.Employee

class MyApplication: Application() {
    var _employee: Employee? = null
    val _appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(
            applicationContext,
            Room.databaseBuilder(
                applicationContext,
                Database::class.java,
                "restaurant_database"
            ).build()
        )
    }
}
val Context.appComponent: AppComponent
get() = when(this) {
    is MyApplication -> _appComponent
    else -> this.applicationContext.appComponent
}
var Context.employee: Employee?
get() = when(this){
    is MyApplication -> _employee
    else -> this.applicationContext.employee
}
set(value) = when(this) {
    is MyApplication -> _employee = value
    else -> this.applicationContext.employee = value
}

