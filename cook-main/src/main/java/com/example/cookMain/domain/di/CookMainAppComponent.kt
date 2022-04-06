package com.example.cookMain.domain.di

import com.example.cookMain.domain.di.modules.ListenersModule
import com.example.core.data.listeners.MenuVersionListenerImpl
import com.example.core.domain.listeners.MenuVersionListener
import dagger.Component

@Component(modules = [ListenersModule::class])
interface CookMainAppComponent {
    fun provideMenVersionListener(): MenuVersionListener
}