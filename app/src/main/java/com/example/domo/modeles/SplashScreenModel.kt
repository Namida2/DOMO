package com.example.domo.modeles

import com.example.domo.modeles.localRepository.SplashScreenLocalRepository
import com.example.domo.modeles.remoteRepository.SplashScreenRemoteRepository
import javax.inject.Inject

class SplashScreenModel @Inject constructor(
    private var localRepository: SplashScreenLocalRepository,
    private var remoteRepository: SplashScreenRemoteRepository
) {

}