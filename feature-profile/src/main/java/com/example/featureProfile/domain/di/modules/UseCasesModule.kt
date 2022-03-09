package com.example.featureProfile.domain.di.modules

import com.example.featureProfile.domain.LeaveAccountUseCase
import com.example.featureProfile.domain.LeaveAccountUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun bindLeaveAccountUseCase(useCase: LeaveAccountUseCaseImpl): LeaveAccountUseCase
}