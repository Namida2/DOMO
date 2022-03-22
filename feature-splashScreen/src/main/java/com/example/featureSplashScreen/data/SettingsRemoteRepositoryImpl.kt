package com.example.featureSplashScreen.data

import com.example.core.domain.Settings
import com.example.core.domain.tools.SimpleTask
import com.example.core.domain.tools.constants.ErrorMessages.defaultErrorMessage
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_GUESTS_COUNT
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_TABLES_COUNT
import com.example.core.domain.tools.constants.FirestoreReferences.settingsDocumentRef
import com.example.core.domain.tools.constants.OtherStringConstants.CAN_NOT_FIND_SETTINGS
import com.example.core.domain.tools.extensions.getExceptionMessage
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import com.example.featureSplashScreen.domain.repositories.SettingsRemoteRepository
import javax.inject.Inject

class SettingsRemoteRepositoryImpl @Inject constructor(
    private val settings: Settings
): SettingsRemoteRepository {
    override fun readSettings(task: SimpleTask) {
        settingsDocumentRef.get().addOnSuccessListener {
            val tablesCount = it.getLong(FIELD_TABLES_COUNT)?.toInt()
            val guestsCount = it.getLong(FIELD_GUESTS_COUNT)?.toInt()
            if(tablesCount != null && guestsCount != null) {
                settings.tablesCount = tablesCount
                settings.guestsCount = guestsCount
                task.onSuccess(Unit)
            } else {
                logE("$this: $CAN_NOT_FIND_SETTINGS")
                task.onError(defaultErrorMessage)
            }
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }
}