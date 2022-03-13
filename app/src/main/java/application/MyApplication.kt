package application

import android.app.Application
import androidx.room.Room
import com.example.core.data.database.Database
import com.example.core.domain.Employee
import com.example.core.domain.di.CoreDepsStore
import com.example.core.domain.tools.constants.SharedPreferencesConstants
import com.example.featureSplashScreen.domain.di.SplashScreenDepsStore
import com.example.domo.viewModels.ViewModelFactory
import di.AppComponent
import di.DaggerAppComponent

class MyApplication : Application() {
    var _employee: Employee = Employee()
        set(value) {
            field.email = value.email
            field.name = value.name
            field.post = value.post
            field.password = value.password
            field.permission = value.permission
        }
    val _appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(
            _employee,
            applicationContext,
            Room.databaseBuilder(
                applicationContext,
                Database::class.java,
                "restaurant_database"
            ).build(),
            getSharedPreferences(
                SharedPreferencesConstants.DB_SETTINGS,
                MODE_PRIVATE
            )
        )
    }

    override fun onCreate() {
        super.onCreate()
        SplashScreenDepsStore.deps = _appComponent
        ViewModelFactory.appComponent = _appComponent
        SplashScreenDepsStore.deps = _appComponent
        CoreDepsStore.deps = SplashScreenDepsStore.appComponent
    }
}

