package di.modules

import dagger.Module
import dagger.Provides
import com.example.core.data.database.Database

@Module
class LocalRepositoryModule {
    @Provides
    fun provideEmployeeDao(database: com.example.core.data.database.Database) = database.employeeDao()

    @Provides
    fun provideMenuDao(database: com.example.core.data.database.Database) = database.menuDao()
}
