package com.example.featureSettings.domain.useCases

import com.example.core.domain.entities.tools.SimpleTask
import com.example.featureSettings.domain.repositories.MenuRemoteRepository
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class SaveMenuUseCase @Inject constructor(
    private val menuRemoteRepository: MenuRemoteRepository
) {
    fun saveNewMenu(targetMenuCollection: CollectionReference, task: SimpleTask) {
        menuRemoteRepository.saveNewMenu(targetMenuCollection, task)
    }
}
