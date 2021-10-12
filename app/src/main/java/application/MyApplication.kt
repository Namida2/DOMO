package application

import android.app.Application
import android.content.Context
import androidx.room.Room
import database.Database
import di.AppComponent
import di.DaggerAppComponent
import entities.Employee


class MyApplication: Application() {
    var _currentEmployee: Employee? = null
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
var Context.currentEmployee: Employee?
get() = when(this) {
    is MyApplication -> _currentEmployee
    else -> this.applicationContext.currentEmployee
}
set(value) {
    (this.applicationContext as MyApplication)._currentEmployee = value
}
