package com.example.cookMain.domain.di

import com.example.core.data.listeners.MenuVersionListenerImpl
import dagger.Component

@Component
interface CookMainAppComponent {
    fun provideMenVersionListener() =
        MenuVersionListenerImpl()
}