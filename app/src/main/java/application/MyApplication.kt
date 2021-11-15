package application

import android.app.Application
import androidx.room.Room
import application.interfaces.MenuHolder
import database.Database
import di.AppComponent
import di.DaggerAppComponent
import entities.Dish
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

