package com.example.featureOrder.domain.di

import com.example.featureOrder.domain.di.modules.FirebaseModule
import com.example.featureOrder.domain.di.modules.RemoteRepositoriesModule
import com.example.featureOrder.domain.di.modules.ServicesModule
import com.example.featureOrder.domain.di.modules.UseCasesModule
import com.example.featureOrder.domain.repositories.OrderMenuDialogRemoteRepository
import com.example.featureOrder.presentation.order.OrderFragment
import com.example.featureOrder.presentation.order.doalogs.orderMenuDialog.useCases.InsertOrderUseCase
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.menu.MenuService
import com.example.waiterCore.domain.order.OrderServiceSub
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [], modules = [ServicesModule::class, UseCasesModule::class, RemoteRepositoriesModule::class, FirebaseModule::class])
interface OrderAppComponent {
    @Component.Builder
    interface Builder {
//        fun provideOrderAppComponentDeps(deps: OrderAppComponentDeps): Builder
        fun build(): OrderAppComponent
    }
    fun inject(fragment: OrderFragment)
    fun provideOrderService() : OrdersService<OrderServiceSub>
    fun provideInsertOrderUseCase() : InsertOrderUseCase
}
