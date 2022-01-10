package di.modules

import dagger.Module
import dagger.Provides
import database.Database
import javax.inject.Singleton

@Module
class LocalRepositoryModule {
    @Provides
    fun provideEmployeeDao(database: Database) = database.employeeDao()

    @Provides
    fun provideMenuDao(database: Database) = database.menuDao()

}
