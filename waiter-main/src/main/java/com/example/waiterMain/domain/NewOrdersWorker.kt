package com.example.waiterMain.domain

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.waiterCore.domain.order.Order
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.Event
import com.example.waiterCore.domain.tools.FirestoreReferences.newOrdersListenerDocumentRef
import com.example.waiterCore.domain.tools.constants.EmployeePosts.COOK
import com.example.waiterCore.domain.tools.constants.FirestoreConstants.FIELD_GUESTS_COUNT
import com.example.waiterCore.domain.tools.constants.FirestoreConstants.FIELD_ORDER_ID
import com.example.waiterCore.domain.tools.constants.FirestoreConstants.FIELD_ORDER_INFO
import com.example.waiterCore.domain.tools.constants.OtherStringConstants.nullOrderInfoMessage
import com.example.waiterCore.domain.tools.extensions.logD
import com.example.waiterCore.domain.tools.extensions.logE
import com.example.waiterMain.R
import com.example.waiterMain.domain.di.WaiterMainDepsStore
import com.example.waiterMain.domain.useCases.ReadNewOrderUseCase
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject

typealias ErrorMessageEvent = Event<ErrorMessage>

class NewOrdersWorker @Inject constructor(
    private val context: Context, params: WorkerParameters,
    private val readNewOrderUseCase: ReadNewOrderUseCase
): CoroutineWorker(context, params) {

    companion object {
        var ordersListener: ListenerRegistration? = null
        private val _event: MutableLiveData<ErrorMessageEvent> = MutableLiveData()
        val event: LiveData<ErrorMessageEvent> = _event
    }

    private val channelId = "0_0"
    private var id = 0
    private lateinit var notificationManager: NotificationManager
    override suspend fun doWork(): Result {
        if (ordersListener != null) return Result.retry()
        createNotificationChannel()
        ordersListener = newOrdersListenerDocumentRef.addSnapshotListener { snapshot, error ->
            when {
                error != null -> {
                    logE("$this: $error")
                    return@addSnapshotListener
                }
                snapshot != null && snapshot.exists() && snapshot.data != null -> {
                    onNewOrder(snapshot.data!!)
                }
                else -> logD("$this: $nullOrderInfoMessage")
            }

        }
        return Result.success()
    }

    private fun onNewOrder(data: MutableMap<String, Any>) {
//        if (WaiterMainDepsStore.currentEmployee!!.post == COOK)
            notificationManager.notify(
                id++, createNotification(data.toString())
            )
        val orderInfo = data[FIELD_ORDER_INFO] as Map<*, *>
        val tableId = orderInfo[FIELD_ORDER_ID] as Int
        val guestCount = data[FIELD_GUESTS_COUNT] as Int
        readNewOrderUseCase.readNewOrder(
            Order(tableId, guestCount)
        ) {
            _event.value = ErrorMessageEvent(it)
        }
    }

    private fun createNotification(message: String): Notification =
        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_email)
            .setContentTitle("New order")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH).build()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = "descriptionText"
            }
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}