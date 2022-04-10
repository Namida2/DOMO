package com.example.core.data.repositorues

import com.example.core.domain.entities.tools.TaskWithPassword
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_PASSWORD
import com.example.core.domain.entities.tools.constants.FirestoreReferences.adminPasswordDocumentRef
import com.example.core.domain.entities.tools.extensions.addOnSuccessListenerWithDefaultFailureHandler
import com.example.core.domain.repositories.AdminPasswordRemoteRepository
import com.google.firebase.firestore.ktx.getField
import javax.inject.Inject

class AdminPasswordRemoteRepositoryImpl @Inject constructor() : AdminPasswordRemoteRepository {
    override fun getPassword(task: TaskWithPassword) {
        adminPasswordDocumentRef.get().addOnSuccessListenerWithDefaultFailureHandler(task) {
            task.onSuccess(
                it.result.getField<String>(FIELD_PASSWORD)
                    ?: return@addOnSuccessListenerWithDefaultFailureHandler
            )
        }
    }
}