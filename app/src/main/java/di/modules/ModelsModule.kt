package di.modules

import com.example.domo.models.*
import com.example.domo.models.interfaces.LogInModelInterface
import com.example.domo.models.interfaces.MenuHolder
import com.example.domo.models.interfaces.RegistrationModelInterface
import com.example.domo.models.interfaces.SplashScreenModelInterface
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
}