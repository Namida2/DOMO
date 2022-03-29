package com.example.core.domain.di.modules

import com.example.core.data.listeners.DeletedOrdersListenerImpl
import com.example.core.domain.listeners.DeletedOrdersListener
import dagger.Binds
import dagger.Module

@Module
interface ListenersModule {
    @Binds
    fun bindDeleteOrderListener(listener: DeletedOrdersListenerImpl): DeletedOrdersListener
}