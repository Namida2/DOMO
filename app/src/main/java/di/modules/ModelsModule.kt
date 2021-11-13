package di.modules

import com.example.domo.models.*
import com.example.domo.models.interfaces.LogInModelInterface
import com.example.domo.models.interfaces.RegistrationModelInterface
import com.example.domo.models.interfaces.SplashScreenModelInterface
import dagger.Binds
import dagger.Module
import extentions.interfaces.BaseObservable

@Module
interface ModelsModule {
    @Binds
    fun bindSplashScreenModel(model: SplashScreenModel): SplashScreenModelInterface
    @Binds
    fun bindRegistrationModel(model: RegistrationModel): RegistrationModelInterface
    @Binds
    fun bindLoginModel(model: LogInModel): LogInModelInterface
    @Binds
    fun bindMenuService(service: MenuService): MenuServiceObservable
}