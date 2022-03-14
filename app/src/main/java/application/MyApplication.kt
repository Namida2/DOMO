package application

import android.app.Application
import androidx.room.Room
import com.example.core.data.database.Database
import com.example.core.domain.di.CoreDepsStore
import com.example.core.domain.tools.constants.SharedPreferencesConstants
import com.example.domo.R
import com.example.featureSplashScreen.domain.di.DaggerSplashScreenAppComponent
import com.example.featureSplashScreen.domain.di.SplashScreenDepsStore
import di.AppComponent
import di.DaggerAppComponent

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
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
    }
}

