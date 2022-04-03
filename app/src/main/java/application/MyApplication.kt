package application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.example.core.data.database.Database
import com.example.core.domain.di.CoreDepsStore
import com.example.core.domain.entities.tools.NetworkConnectionListener
import com.example.core.domain.entities.tools.constants.SharedPreferencesConstants
import com.example.domo.R
import com.example.featureSplashScreen.domain.di.DaggerSplashScreenAppComponent
import com.example.featureSplashScreen.domain.di.SplashScreenDepsStore

class MyApplication : Application() {

    override fun onCreate() {
        NetworkConnectionListener.registerCallback(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        SplashScreenDepsStore.appComponent =
            DaggerSplashScreenAppComponent
                .builder()
                .provideEmployee(SplashScreenDepsStore.currentEmployee)
                .provideContext(applicationContext)
                .provideDatabase(
                    Room.databaseBuilder(
                        applicationContext,
                        Database::class.java,
                        resources.getString(R.string.databaseName)
                    ).build()
                ).provideSharedPreferences(
                    getSharedPreferences(
                        SharedPreferencesConstants.DB_SETTINGS,
                        MODE_PRIVATE
                    )
                ).build()
        CoreDepsStore.deps = SplashScreenDepsStore.appComponent
        super.onCreate()
    }
}

