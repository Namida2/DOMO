package di.modules

import application.interfaces.MenuHolder
import com.example.domo.models.*
import com.example.domo.models.interfaces.LogInModelInterface
import com.example.domo.models.interfaces.RegistrationModelInterface
import com.example.domo.models.interfaces.SplashScreenModelInterface
import dagger.Binds
import dagger.Module

@Module
interface ModelsModule {
    @Binds
    fun bindSplashScreenModel(model: SplashScreenModel): SplashScreenModelInterface
    @Binds
    fun bindRegistrationModel(model: RegistrationModel): RegistrationModelInterface
    @Binds
    fun bindLoginModel(model: LogInModel): LogInModelInterface
    @Binds
    fun bindMenuHolder(menuHolder: MenuService): MenuHolder
}