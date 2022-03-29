package com.example.featureSplashScreen.domain.useCases

import com.example.core.domain.entities.tools.SimpleTask
import com.example.featureSplashScreen.domain.repositories.SettingsRemoteRepository
import javax.inject.Inject

class ReadSettingsUseCase @Inject constructor(
    private val settingsRemoteRepository: SettingsRemoteRepository
) {
    fun readSettings(task: SimpleTask) {
        settingsRemoteRepository.readSettings(task)
    }
}