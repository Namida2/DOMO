package com.example.waiterMain.domain.di

import android.content.Context
import com.example.core.domain.entities.Employee
import com.example.core.domain.di.modules.RemoteRepositoryModule
import com.example.core.domain.di.modules.UseCasesModule
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
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
}

interface WaiterMainDeps {
    val currentEmployee: Employee?
    val context: Context
    val ordersService: OrdersService<OrdersServiceSub>
}
