package com.example.featureOrder.domain.di

import com.example.featureOrder.domain.di.modules.FirebaseModule
import com.example.featureOrder.domain.di.modules.RemoteRepositoriesModule
import com.example.featureOrder.domain.di.modules.ServicesModule
import com.example.featureOrder.domain.di.modules.UseCasesModule
import com.example.featureOrder.presentation.order.OrderFragment
import com.example.featureOrder.domain.useCases.InsertOrderUseCase
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrdersServiceSub
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
    fun provideInsertOrderUseCase() : InsertOrderUseCase
}

interface OrderAppComponentDeps {
    val ordersService: OrdersService<OrdersServiceSub>
}
