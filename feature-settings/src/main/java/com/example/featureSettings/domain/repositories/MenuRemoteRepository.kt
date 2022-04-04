package com.example.featureSettings.domain.repositories

import com.example.core.domain.entities.tools.SimpleTask

interface MenuRemoteRepository {
    fun saveNewMenu(task: SimpleTask)
    fun saveCurrentMenuAsDefault(task: SimpleTask)
}