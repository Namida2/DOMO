package com.example.featureSettings.domain.repositories

import com.example.core.domain.tools.SimpleTask

interface MenuRemoteRepository {
    fun saveNewMenu(task: SimpleTask)
}