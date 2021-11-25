package di.modules

import com.example.domo.models.*
import com.example.domo.models.authorisation.LogInModel
import com.example.domo.models.authorisation.RegistrationModel
import com.example.domo.models.interfaces.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ModelsModule {
    @Binds
    @Singleton
    fun bindSplashScreenModel(model: SplashScreenModel): SplashScreenModelInterface
    @Binds
    @Singleton
    fun bindRegistrationModel(model: RegistrationModel): RegistrationModelInterface
    @Binds
    @Singleton
    fun bindLoginModel(model: LogInModel): LogInModelInterface
    @Binds
    @Singleton
    fun bindMenuHolder(menuHolder: MenuService): MenuHolder
    @Binds
    @Singleton
    fun bindMenuLocalRepository(menuHolder: MenuService): MenuLocalRepository
    @Binds
    @Singleton
    fun bindMenuDialogModule(menuDialogModel: MenuDialogModel): MenuDialogInterface
}