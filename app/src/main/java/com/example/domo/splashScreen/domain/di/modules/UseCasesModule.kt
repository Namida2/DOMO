package com.example.domo.splashScreen.domain.di.modules

import com.example.domo.authorization.domain.LogInUseCase
import com.example.domo.authorization.domain.LogInUseCaseImpl
import com.example.domo.registration.domain.GetPostItemsUseCase
import com.example.domo.registration.domain.GetPostItemsUseCaseImpl
import com.example.domo.registration.domain.RegistrationUseCase
import com.example.domo.registration.domain.RegistrationUseCaseImpl
import com.example.domo.splashScreen.domain.GetCurrentEmployeeUseCase
import com.example.domo.splashScreen.domain.GetCurrentEmployeeUseCaseImpl
import com.example.domo.splashScreen.domain.ReadMenuUseCase
import com.example.domo.splashScreen.domain.ReadMenuUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun bindReadMenuUseCase(useCase: ReadMenuUseCaseImpl): ReadMenuUseCase
    @Binds
    fun bindGetCurrentEmployeeUseCase(useCase: GetCurrentEmployeeUseCaseImpl): GetCurrentEmployeeUseCase
    @Binds
    fun bindLogInUseCase(useCase: LogInUseCaseImpl): LogInUseCase
    @Binds
    fun bindRegistrationUseCase(useCase: RegistrationUseCaseImpl): RegistrationUseCase
    @Binds
    fun bindGetPostItemsUseCase(useCase: GetPostItemsUseCaseImpl): GetPostItemsUseCase
}