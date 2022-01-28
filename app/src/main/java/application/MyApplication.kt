package application

import android.app.Application
import androidx.room.Room
import com.example.domo.splashScreen.domain.di.DaggerSplashScreenAppComponent
import com.example.domo.viewModels.ViewModelFactory
import com.example.domo.splashScreen.domain.di.SplashScreenDepsStore
import com.example.waiterCore.data.database.Database
import di.AppComponent
import di.DaggerAppComponent
import com.example.waiterCore.domain.Employee
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
        com.example.domo.splashScreen.domain.ViewModelFactory.appComponent = DaggerSplashScreenAppComponent.builder().putDeps(_appComponent).build()
    }
}

