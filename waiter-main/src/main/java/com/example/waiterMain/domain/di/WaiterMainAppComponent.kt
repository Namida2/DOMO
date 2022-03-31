package com.example.waiterMain.domain.di

import com.example.core.data.listeners.MenuVersionListenerImpl
import com.example.core.domain.di.modules.RemoteRepositoryModule
import com.example.core.domain.di.modules.UseCasesModule
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.Settings
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.listeners.MenuVersionListener
import com.example.core.domain.useCases.ReadNewOrderUseCase
import dagger.Component

@Component(
    dependencies = [WaiterMainDeps::class],
    modules = [RemoteRepositoryModule::class, UseCasesModule::class]
)
interface WaiterMainAppComponent {
    @Component.Builder
    interface Builder {
        fun provideWaiterMainDeps(deps: WaiterMainDeps): Builder
        fun build(): WaiterMainAppComponent
    }
    fun provideReadNewOrderUseCase(): ReadNewOrderUseCase
    fun provideMenuVersionListener() = MenuVersionListenerImpl()
}

interface WaiterMainDeps {
    val settings: Settings
    val currentEmployee: Employee?
    val ordersService: OrdersService
}
