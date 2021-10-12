package application

import android.app.Application
import android.content.Context
import androidx.room.Room
import database.Database
import di.AppComponent
import di.DaggerAppComponent



class MyApplication: Application() {
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
