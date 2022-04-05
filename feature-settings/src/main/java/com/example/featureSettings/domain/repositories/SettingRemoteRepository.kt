package com.example.featureSettings.domain.repositories

import com.example.core.domain.entities.tools.SimpleTask

interface SettingRemoteRepository {
    fun saveNewSSettings(maxTablesCount: Int, maxGuestsCount: Int, task: SimpleTask)
}