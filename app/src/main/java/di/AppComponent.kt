package di

import android.content.Context
import android.content.SharedPreferences
import com.example.domo.models.OrderServiceSub
import com.example.domo.models.OrdersService
import com.example.domo.models.interfaces.LogInModelInterface
import com.example.domo.models.interfaces.MenuDialogInterface
import com.example.domo.models.interfaces.RegistrationModelInterface
import com.example.domo.models.interfaces.SplashScreenModelInterface
import com.example.domo.views.activities.SplashScreenActivity
import com.example.domo.views.fragments.TablesFragment
import com.example.domo.views.dialogs.MenuBottomSheetDialog
import dagger.BindsInstance
import dagger.Component
import database.Database
import di.modules.LocalRepositoryModule
import di.modules.ModelsModule
import di.modules.FirebaseModule
import di.modules.RemoteRepositoriesModule
import entities.interfaces.OrderServiceInterface
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

    fun provideSplashScreenModel(): SplashScreenModelInterface
    fun provideRegistrationModel(): RegistrationModelInterface
    fun provideLogInModel(): LogInModelInterface
    fun provideMenuDialogModel(): MenuDialogInterface
    fun provideOrderService(): OrderServiceInterface<OrderServiceSub>

    fun inject(splashScreenActivity: SplashScreenActivity)
    fun inject(fragment: TablesFragment)
    fun inject(fragment: MenuBottomSheetDialog)

}




