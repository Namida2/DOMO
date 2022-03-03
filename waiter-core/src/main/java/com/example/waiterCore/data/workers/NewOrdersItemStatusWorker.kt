package com.example.waiterCore.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.core.domain.tools.FirestoreReferences.orderItemsStateListenerDocumentRef
import com.example.core.domain.tools.constants.OtherStringConstants
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import com.google.firebase.firestore.ListenerRegistration

class NewOrdersItemStatusWorker(
    private val context: Context, params: WorkerParameters
): CoroutineWorker(context, params) {

    companion object {
        val newOrderItemsStateListener: ListenerRegistration? = null
    }

    override suspend fun doWork(): Result {
        if(newOrderItemsStateListener != null) return Result.retry()
        orderItemsStateListenerDocumentRef.addSnapshotListener { snapshot, error ->
            when {
                error != null -> {
                    logE("$this: $error")
                    return@addSnapshotListener
                }
                snapshot != null && snapshot.exists() && snapshot.data != null -> {
//                    onNewOrder(snapshot.data!!)
                }
                else -> logD("$this: ${OtherStringConstants.NULL_ORDER_INFO_MESSAGE}")
            }
        }
        return Result.success()
    }

}