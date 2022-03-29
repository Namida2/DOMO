package com.example.featureSplashScreen.domain.repositories

import com.example.core.domain.entities.tools.SimpleTask

interface SettingsRemoteRepository {
    fun readSettings(task: SimpleTask)
}