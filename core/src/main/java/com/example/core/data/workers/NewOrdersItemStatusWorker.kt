package com.example.core.data.workers

import android.app.NotificationManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.core.domain.di.CoreDepsStore
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_IS_READY
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_ID
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_ITEM_ID
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_ITEM_INFO
import com.example.core.domain.entities.tools.constants.FirestoreReferences.orderItemsStateListenerDocumentRef
import com.example.core.domain.entities.tools.constants.OtherStringConstants
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.notofications.NotificationsTools
import com.google.firebase.firestore.ListenerRegistration

class NewOrdersItemStatusWorker(
    private val context: Context, params: WorkerParameters
) : CoroutineWorker(context, params) {

    var isFirstNotification = true
    private var id = 1
    private val orderService: OrdersService = CoreDepsStore.deps.ordersService
    private var notificationManager: NotificationManager? = null

    companion object {
        var newOrderItemsStateListener: ListenerRegistration? = null
    }

    override suspend fun doWork(): Result {
        if (newOrderItemsStateListener != null) return Result.retry()
        notificationManager = NotificationsTools.createNotificationChannel(context)
        newOrderItemsStateListener =
            orderItemsStateListenerDocumentRef.addSnapshotListener { snapshot, error ->
                when {
                    error != null -> {
                        logE("$this: $error")
                        newOrderItemsStateListener?.remove()
                        newOrderItemsStateListener = null
                        return@addSnapshotListener
                    }
                    snapshot != null && snapshot.exists() && snapshot.data != null -> {
                        if (!isFirstNotification)
                            setNewOrderItemStatus(snapshot.data!!)
                        isFirstNotification = false
                    }
                    else -> logD("$this: ${OtherStringConstants.NULL_ORDER_INFO_MESSAGE}")
                }
            }
        return Result.retry()
    }

    private fun setNewOrderItemStatus(data: Map<String, Any>) {
        val orderItemInfo = data[FIELD_ORDER_ITEM_INFO] as Map<*, *>
        val orderId = (orderItemInfo[FIELD_ORDER_ID] as? Long)?.toInt() ?: return
        val orderItemId = orderItemInfo[FIELD_ORDER_ITEM_ID] as? String ?: return
        val isReady = orderItemInfo[FIELD_IS_READY] as? Boolean ?: return
        orderService.changeOrderItemStatus(orderId, orderItemId, isReady)
//        if (CoreDepsStore.deps.currentEmployee!!.post == WAITER)
        notificationManager?.notify(
            id++,
            NotificationsTools.createNotification(context, data.toString())
        )
    }

}