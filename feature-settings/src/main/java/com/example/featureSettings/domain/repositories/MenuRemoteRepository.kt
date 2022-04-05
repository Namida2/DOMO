package com.example.featureSettings.domain.repositories

import com.example.core.domain.entities.tools.SimpleTask
import com.google.firebase.firestore.CollectionReference

interface MenuRemoteRepository {
    fun saveNewMenu(targetMenuCollection: CollectionReference, task: SimpleTask)
}