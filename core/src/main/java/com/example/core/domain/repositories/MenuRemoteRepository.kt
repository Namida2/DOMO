package com.example.core.domain.repositories

import com.example.core.domain.entities.tools.SimpleTask
import com.google.firebase.firestore.CollectionReference

interface MenuRemoteRepository {
    fun readNewMenu(menuCollectionRef: CollectionReference, task: SimpleTask)
    fun readMenuVersion(onComplete: (version: Long) -> Unit)
}