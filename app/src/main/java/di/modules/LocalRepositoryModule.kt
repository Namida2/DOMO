package di.modules

import dagger.Module
import dagger.Provides
import com.example.waiterCore.data.database.Database

@Module
class LocalRepositoryModule {
    @Provides
    fun provideEmployeeDao(database: Database) = database.employeeDao()

    @Provides
    fun provideMenuDao(database: Database) = database.menuDao()
}
