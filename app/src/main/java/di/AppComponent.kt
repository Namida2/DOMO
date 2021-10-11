package di

import android.content.Context

import androidx.room.Room
import com.example.domo.modeles.SplashScreenModel
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import database.Database
import javax.inject.Singleton

@Singleton
@Component(modules = [LocalRepositoryModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun provideSplashScreenModel(): SplashScreenModel

}

@Module
class LocalRepositoryModule {
    @Provides
    fun provideDatabase(context: Context): Database = Room.databaseBuilder(
        context,
        Database::class.java,
        "restaurant_database"
    ).build()


}


