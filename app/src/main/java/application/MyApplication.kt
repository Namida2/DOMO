package application

import android.app.Application
import androidx.room.Room
import com.example.domo.splashScreen.domain.di.DaggerSplashScreenAppComponent
import com.example.domo.splashScreen.domain.di.SplashScreenDepsStore
import com.example.domo.viewModels.ViewModelFactory
import com.example.waiterCore.data.database.Database
import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.menu.MenuService
import com.example.waiterMain.domain.di.WaiterMainDepsStore
import di.AppComponent
import di.DaggerAppComponent
import com.example.waiterCore.domain.tools.constants.SharedPreferencesConstants

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
        com.example.domo.splashScreen.domain.ViewModelFactory.appComponent =
            DaggerSplashScreenAppComponent.builder().provideDeps(_appComponent)
                .provideMenuService(MenuService).build()
        WaiterMainDepsStore.deps = com.example.domo.splashScreen.domain.ViewModelFactory.appComponent
        WaiterMainDepsStore.currentEmployee = _employee
    }
}

