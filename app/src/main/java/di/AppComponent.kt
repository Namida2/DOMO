package di

import android.content.Context
import com.example.domo.models.LogInModel
import com.example.domo.models.MenuService
import com.example.domo.models.RegistrationModel
import com.example.domo.models.SplashScreenModel
import com.example.domo.views.SplashScreenActivity
import dagger.BindsInstance
import dagger.Component
import database.Database
import di.modules.LocalRepositoryModule
import di.modules.ModelsModule
import di.modules.RemoteRepositoryModule
import javax.inject.Singleton

@Singleton
@Component(modules = [LocalRepositoryModule::class, RemoteRepositoryModule::class, ModelsModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance database: Database,
        ): AppComponent
    }

    fun provideSplashScreenModel(): SplashScreenModel
    fun provideRegistrationModel(): RegistrationModel
    fun provideLogInModel(): LogInModel
    fun inject(splashScreenActivity: SplashScreenActivity)

}




