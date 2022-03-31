package com.example.core.data.listeners

import com.example.core.domain.entities.tools.constants.FirestoreConstants
import com.example.core.domain.entities.tools.constants.FirestoreReferences.menuDocumentRef
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.domain.listeners.MenuVersionListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class MenuVersionListenerImpl @Inject constructor() : MenuVersionListener {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val menuVersionChanges = callbackFlow {
        var isFirstData = true
        val subscription = menuDocumentRef.addSnapshotListener { value, error ->
            when {
                error != null -> {
                    logE("$this: $error")
                    return@addSnapshotListener
                }
                value != null && value.exists() && value.data != null -> {
                    if (isFirstData) {
                        isFirstData = false
                        return@addSnapshotListener
                    }
                    val menuVersion = (value.get(FirestoreConstants.FIELD_MENU_VERSION) as? Long)
                    trySend(menuVersion ?: return@addSnapshotListener)
                }
            }
        }
        awaitClose{
            subscription.remove()
        }
    }
}