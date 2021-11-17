package di.modules

import com.example.domo.models.remoteRepository.SplashScreenRemoteRepository
import com.example.domo.models.remoteRepository.interfaces.SSRemoteRepositoryInterface
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RemoteRepositoriesModule {
    @Binds
    @Singleton
    fun provideSplashScreenRemoteRepository(repository: SplashScreenRemoteRepository): SSRemoteRepositoryInterface
}