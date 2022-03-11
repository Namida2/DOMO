package com.example.featureRegistration.domain.di.modules

import com.example.domo.registration.domain.GetPostItemsUseCase
import com.example.domo.registration.domain.GetPostItemsUseCaseImpl
import com.example.featureRegistration.domain.useCases.RegistrationUseCase
import com.example.featureRegistration.domain.useCases.RegistrationUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun provideGetPostItemUseCase(useCase: GetPostItemsUseCaseImpl): GetPostItemsUseCase
    @Binds
    fun provideRegistrationUseCase(useCase: RegistrationUseCaseImpl): RegistrationUseCase
}