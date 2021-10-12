package com.example.domo.models

import com.example.domo.models.localRepository.SplashScreenLocalRepository
import com.example.domo.models.remoteRepository.SplashScreenRemoteRepository
import javax.inject.Inject

class SplashScreenModel @Inject constructor(
    private var localRepository: SplashScreenLocalRepository,
    private var remoteRepository: SplashScreenRemoteRepository
) {
//    suspend fun getCurrentUser(): String {
//        localRepository.getCurrentEmployee()
//    }
}