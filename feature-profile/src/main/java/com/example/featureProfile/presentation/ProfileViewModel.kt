package com.example.featureProfile.presentation

import androidx.lifecycle.ViewModel
import com.example.featureProfile.domain.LeaveAccountUseCase

class ProfileViewModel(
    private val leaveAccountUseCase: LeaveAccountUseCase
): ViewModel() {

    fun leaveThisAccount(){
        leaveAccountUseCase.leaveThisAccount()
    }
}