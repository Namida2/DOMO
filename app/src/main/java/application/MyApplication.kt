package application

import android.app.Application
import androidx.room.Room
import com.example.domo.viewModels.ViewModelFactory
import com.example.feature_splashscreen.domain.di.SplashScreenDepsStore
import com.example.waiter_core.data.database.Database
import di.AppComponent
import di.DaggerAppComponent
import com.example.waiter_core.domain.Employee
import entities.constants.SharedPreferencesConstants

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
        SplashScreenDepsStore.deps = _appComponent
        ViewModelFactory.appComponent = _appComponent
    }
}

