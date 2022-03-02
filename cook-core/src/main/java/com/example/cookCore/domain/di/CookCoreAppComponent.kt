package com.example.cookCore.domain.di

import com.example.core.domain.di.modules.UseCasesModule
import dagger.Component

@Component(modules = [UseCasesModule::class])
interface CookCoreAppComponent {

}
