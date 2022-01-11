package di.modules

import dagger.Module
import dagger.Provides
import com.example.waiter_core.data.database.Database

@Module
class LocalRepositoryModule {
    @Provides
    fun provideEmployeeDao(database: Database) = database.employeeDao()

    @Provides
    fun provideMenuDao(database: Database) = database.menuDao()

}
