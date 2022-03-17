package com.example.core.data.workers

import android.app.NotificationManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.core.domain.di.CoreDepsStore
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.notofications.NotificationsTools
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.constants.FirestoreReferences.orderItemsStateListenerDocumentRef
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_ID
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_ITEM_ID
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_ITEM_INFO
import com.example.core.domain.tools.constants.OtherStringConstants
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import com.google.firebase.firestore.ListenerRegistration

class NewOrdersItemStatusWorker(
    private val context: Context, params: WorkerParameters
) : CoroutineWorker(context, params) {

    var isFirstNotification = true
    private var id = 1
    private val orderService: OrdersService<OrdersServiceSub> =
        CoreDepsStore.deps.ordersService
    private var notificationManager: NotificationManager? = null
    companion object {
        val newOrderItemsStateListener: ListenerRegistration? = null
    }

    override suspend fun doWork(): Result {
        if (newOrderItemsStateListener != null) return Result.retry()
        notificationManager = NotificationsTools.createNotificationChannel(context)
        orderItemsStateListenerDocumentRef.addSnapshotListener { snapshot, error ->
            when {
                error != null -> {
                    logE("$this: $error")
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
        orderService.changeOrderItemStatus(orderId, orderItemId)
//        if (CoreDepsStore.deps.currentEmployee!!.post == WAITER)
            notificationManager?.notify( id++,
                NotificationsTools.createNotification(context, data.toString())
            )
    }

}