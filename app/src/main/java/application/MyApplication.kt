package application

import android.app.Application
import androidx.room.Room
import com.example.domo.viewModels.ViewModelFactory

import constants.SharedPreferencesConstants
import database.Database
import di.AppComponent
import di.DaggerAppComponent

import entities.Employee

class MyApplication : Application() {
    var _employee: Employee? = null
    val _appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(
            applicationContext,
            Room.databaseBuilder(
                applicationContext,
                Database::class.java,
                "restaurant_database"
            ).build(),
            getSharedPreferences(SharedPreferencesConstants.DB_SETTINGS, MODE_PRIVATE)
        )
    }

    override fun onCreate() {
        super.onCreate()
        ViewModelFactory.appComponent = _appComponent
    }
}

