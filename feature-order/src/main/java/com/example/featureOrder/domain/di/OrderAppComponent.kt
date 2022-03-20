package com.example.featureOrder.domain.di

import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.featureOrder.domain.di.modules.FirebaseModule
import com.example.featureOrder.domain.di.modules.RemoteRepositoriesModule
import com.example.featureOrder.domain.di.modules.ServicesModule
import com.example.featureOrder.domain.di.modules.UseCasesModule
import com.example.featureOrder.domain.useCases.InsertOrderUseCase
import com.example.featureOrder.presentation.order.OrderFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [OrderAppComponentDeps::class],
    modules = [ServicesModule::class, UseCasesModule::class, RemoteRepositoriesModule::class, FirebaseModule::class]
)
interface OrderAppComponent {
    @Component.Builder
    interface Builder {
        fun provideOrderAppComponentDeps(deps: OrderAppComponentDeps): Builder
        fun build(): OrderAppComponent
    }

    fun inject(fragment: OrderFragment)
    fun provideOrderService(): OrdersService<OrdersServiceSub>
    fun provideInsertOrderUseCase(): InsertOrderUseCase
}

interface OrderAppComponentDeps {
    val currentEmployee: Employee?
    val ordersService: OrdersService<OrdersServiceSub>
}
