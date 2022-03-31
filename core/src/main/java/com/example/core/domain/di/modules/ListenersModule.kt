package com.example.core.domain.di.modules

import com.example.core.data.listeners.DeletedOrdersListenerImpl
import com.example.core.data.listeners.MenuVersionListenerImpl
import com.example.core.domain.listeners.DeletedOrdersListener
import com.example.core.domain.listeners.MenuVersionListener
import dagger.Binds
import dagger.Module

@Module
interface ListenersModule {
    @Binds
    fun bindDeleteOrderListener(listener: DeletedOrdersListenerImpl): DeletedOrdersListener
    @Binds
    fun bindMenuVersionListener(listenerImpl: MenuVersionListenerImpl): MenuVersionListener
}