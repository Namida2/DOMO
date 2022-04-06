package com.example.cookMain.domain.di.modules

import com.example.core.data.listeners.MenuVersionListenerImpl
import com.example.core.domain.listeners.MenuVersionListener
import dagger.Binds
import dagger.Module

@Module
interface ListenersModule {
    @Binds
    fun provideMenVersionListener(listener: MenuVersionListenerImpl): MenuVersionListener

}