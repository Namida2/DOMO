package com.example.featureCurrentOrders.domain.di

import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.featureCurrentOrders.domain.di.modules.RemoteRepositoriesModule
import com.example.featureCurrentOrders.domain.useCases.DeleteOrderUseCase
import com.example.featureCurrentOrders.presentation.currentOrders.CurrentOrdersFragment
import dagger.Component

@Component(
    dependencies = [CurrentOrdersAppComponentDeps::class],
    modules = [RemoteRepositoriesModule::class]
)
interface CurrentOrdersAppComponent {

    @Component.Builder
    interface Builder {
        fun provideCurrentOrdersDeps(deps: CurrentOrdersAppComponentDeps): Builder
        fun build(): CurrentOrdersAppComponent
    }

    fun inject(fragment: CurrentOrdersFragment)
    fun provideDeleteOrderUseCase(): DeleteOrderUseCase
}

interface CurrentOrdersAppComponentDeps {
    val currentEmployee: Employee?
    val ordersService: OrdersService
}