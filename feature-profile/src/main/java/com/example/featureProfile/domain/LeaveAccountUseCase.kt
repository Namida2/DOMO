package com.example.featureProfile.domain

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LeaveAccountUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): LeaveAccountUseCase {
    override fun leaveThisAccount() {
        TODO("Not yet implemented")
    }
}

interface LeaveAccountUseCase {
    fun leaveThisAccount()
}