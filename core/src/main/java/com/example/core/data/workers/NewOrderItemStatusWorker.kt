package com.example.core.data.workers

import android.app.NotificationManager
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.core.R
import com.example.core.domain.di.CoreDepsStore
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_IS_READY
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_ID
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_ITEM_ID
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_ITEM_INFO
import com.example.core.domain.entities.tools.constants.FirestoreConstants.ORDER_ITEM_ID_DELIMITER
import com.example.core.domain.entities.tools.constants.FirestoreReferences.orderItemsStateListenerDocumentRef
import com.example.core.domain.entities.tools.constants.OtherStringConstants
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.notofications.NotificationsTools
import com.example.core.domain.notofications.NotificationsTools.createNotificationChannel
import com.google.firebase.firestore.ListenerRegistration

class NewOrderItemStatusWorker(
    private val context: Context, params: WorkerParameters
) : Worker(context, params) {

    var isFirstNotification = true
    private var id = 1
    private val orderService: OrdersService = CoreDepsStore.deps.ordersService
    private var notificationManager: NotificationManager? = null

    companion object {
        const val NEW_ORDER_ITEM_STATUS_WORKER_TAG = "NEW_ORDER_ITEM_STATUS_WORKER_TAG"
        var needToShowNotifications = true
        private var newOrderItemsStateListener: ListenerRegistration? = null
    }

    override fun doWork(): Result {
        if (isStopped) return Result.success()
        if (newOrderItemsStateListener != null) return Result.retry()
        notificationManager = createNotificationChannel(context)
        newOrderItemsStateListener =
            orderItemsStateListenerDocumentRef.addSnapshotListener { snapshot, error ->
                when {
                    error != null -> {
                        logE("$this: $error")
                        newOrderItemsStateListener?.remove()
                        newOrderItemsStateListener = null
                        isFirstNotification = true
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
        val dishId = orderItemId.substring(0, orderItemId.indexOf(ORDER_ITEM_ID_DELIMITER))
        orderService.changeOrderItemStatus(orderId, orderItemId, isReady)
//        if (CoreDepsStore.deps.currentEmployee!!.post == WAITER)
        if (needToShowNotifications)
            notificationManager?.notify(
                id++,
                NotificationsTools.createNotification(
                    context,
                    context.resources.getString(R.string.table, orderId),
                    context.resources.getString(R.string.dishIsReady) + MenuService.getDishById(
                        dishId.toInt()
                    ).name
                )
            )
    }

}