package com.example.core.domain.di

import android.content.Context
import com.example.core.domain.entities.Employee
import com.example.core.domain.di.modules.RemoteRepositoryModule
import com.example.core.domain.di.modules.UseCasesModule
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.useCases.ReadNewOrderUseCase
import dagger.Component

@Component(
    dependencies = [CoreAppComponentDeps::class],
    modules = [UseCasesModule::class, RemoteRepositoryModule::class]
)
interface CoreAppComponent {
    fun provideReadNewOrderUseCase(): ReadNewOrderUseCase
}

interface CoreAppComponentDeps {
    val context: Context
    val currentEmployee: Employee?
    val ordersService: OrdersService<OrdersServiceSub>
}