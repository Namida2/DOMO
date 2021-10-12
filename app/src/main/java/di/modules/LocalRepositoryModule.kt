package di.modules

import dagger.Module
import dagger.Provides
import database.Database
import javax.inject.Singleton

@Module
class LocalRepositoryModule {
    @Provides
    @Singleton
    fun provideEmployeeDao(database: Database) = database.employeeDao()

}
