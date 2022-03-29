package com.example.featureSettings.domain.useCases

import com.example.core.domain.entities.tools.SimpleTask
import com.example.featureSettings.domain.repositories.MenuRemoteRepository
import javax.inject.Inject

//TODO: Save new menu //STOPPED//
class SaveMenuUseCase @Inject constructor(
    private val menuRemoteRepository: MenuRemoteRepository
) {
    fun saveNewMenu(task: SimpleTask) {
        menuRemoteRepository.saveNewMenu(task)
    }
}
