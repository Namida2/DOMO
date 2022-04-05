package com.example.featureSettings.data.remositories

import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_GUESTS_COUNT
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_TABLES_COUNT
import com.example.core.domain.entities.tools.constants.FirestoreReferences.settingsDocumentRef
import com.example.core.domain.entities.tools.extensions.addOnSuccessListenerWithDefaultFailureHandler
import com.example.featureSettings.domain.repositories.SettingRemoteRepository
import javax.inject.Inject

class SettingRemoteRepositoryImpl @Inject constructor(): SettingRemoteRepository {

    override fun saveNewSSettings(maxTablesCount: Int, maxGuestsCount: Int, task: SimpleTask) {
        settingsDocumentRef.update(
            mapOf(
                FIELD_TABLES_COUNT to maxTablesCount,
                FIELD_GUESTS_COUNT to maxGuestsCount
            )
        ).addOnSuccessListenerWithDefaultFailureHandler(task) { task.onSuccess(Unit) }
    }

}