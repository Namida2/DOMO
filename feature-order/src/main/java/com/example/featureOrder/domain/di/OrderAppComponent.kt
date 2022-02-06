package com.example.featureOrder.domain.di

import com.example.featureOrder.presentation.order.OrderFragment
import com.example.waiterCore.domain.menu.MenuService
import dagger.Component

@Component(dependencies = [OrderAppComponentDeps::class])
interface OrderAppComponent {
    @Component.Builder
    interface Builder {
        fun provideOrderAppComponentDeps(deps: OrderAppComponentDeps): Builder
        fun build(): OrderAppComponent
    }
    fun inject(fragment: OrderFragment)
}

interface OrderAppComponentDeps {
    val menuService: MenuService
}