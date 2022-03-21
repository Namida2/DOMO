package com.example.featureSplashScreen.domain.repositories

import com.example.core.domain.tools.SimpleTask

interface SettingsRemoteRepository {
    fun readSettings(task: SimpleTask)
}