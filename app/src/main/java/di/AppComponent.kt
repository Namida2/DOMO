package di

import android.content.Context
import android.content.SharedPreferences
import com.example.domo.models.LogInModel
import com.example.domo.models.RegistrationModel
import com.example.domo.models.SplashScreenModel
import com.example.domo.views.SplashScreenActivity
import dagger.BindsInstance
import dagger.Component
import database.Database
import di.modules.LocalRepositoryModule
import di.modules.ModelsModule
import di.modules.FirebaseModule
import di.modules.RemoteRepositoriesModule
import javax.inject.Singleton

@Singleton
@Component(modules = [LocalRepositoryModule::class, FirebaseModule::class, ModelsModule::class, RemoteRepositoriesModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance database: Database,
            @BindsInstance sharedPreferences: SharedPreferences,
        ): AppComponent
    }

    fun provideSplashScreenModel(): SplashScreenModel
    fun provideRegistrationModel(): RegistrationModel
    fun provideLogInModel(): LogInModel
    fun inject(splashScreenActivity: SplashScreenActivity)

}




