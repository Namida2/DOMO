package com.example.featureSettings.domain.useCases

import com.example.core.domain.entities.Settings
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import com.example.featureSettings.domain.repositories.SettingRemoteRepository
import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor(
    private val settings: Settings,
    private val settingRemoteRepository: SettingRemoteRepository
) {
    fun saveNewSettings(maxTablesCount: Int, maxGuestsCount: Int, task: SimpleTask) {
        settingRemoteRepository.saveNewSSettings(maxTablesCount, maxGuestsCount, object: SimpleTask {
            override fun onSuccess(result: Unit) {
                settings.tablesCount = maxTablesCount
                settings.guestsCount = maxGuestsCount
                task.onSuccess(Unit)
            }
            override fun onError(message: ErrorMessage?) {
                task.onError(message)
            }
        })
    }
}