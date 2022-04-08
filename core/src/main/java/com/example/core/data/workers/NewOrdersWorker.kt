package com.example.core.data.workers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.work.*
import androidx.work.PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
import com.example.core.R
import com.example.core.domain.di.CoreDepsStore
import com.example.core.domain.entities.order.Order
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_GUESTS_COUNT
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_ID
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_ORDER_INFO
import com.example.core.domain.entities.tools.constants.FirestoreReferences.newOrdersListenerDocumentRef
import com.example.core.domain.entities.tools.constants.OtherStringConstants.NULL_ORDER_INFO_MESSAGE
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.domain.notofications.NotificationsTools.createNotification
import com.example.core.domain.notofications.NotificationsTools.createNotificationChannel
import com.example.core.domain.useCases.ReadNewOrderUseCase
import com.google.firebase.firestore.ListenerRegistration
import java.util.concurrent.TimeUnit

class NewOrdersWorker(
    private val context: Context, params: WorkerParameters
) : Worker(context, params) {

    private var id = 0
    private var notificationManager: NotificationManager? = null
    private var readNewOrderUseCase: ReadNewOrderUseCase =
        CoreDepsStore.appComponent.provideReadNewOrderUseCase()

    companion object {
        const val NEW_ORDERS_WORKER_TAG = "NEW_ORDERS_WORKER_TAG"
        var needToShowNotifications = true
        private var isFirstNotification = true
        var ordersListener: ListenerRegistration? = null
    }

    override fun doWork(): Result {
        if (isStopped) return Result.success()
        if (ordersListener != null) return Result.retry()
        notificationManager = createNotificationChannel(context)
        ordersListener = newOrdersListenerDocumentRef.addSnapshotListener { snapshot, error ->
            when {
                error != null -> {
                    logE("$this: $error")
                    ordersListener?.remove()
                    ordersListener = null
                    isFirstNotification = true
                    return@addSnapshotListener
                }
                snapshot != null && snapshot.exists() && snapshot.data != null -> {
                    if (!isFirstNotification)
                        onNewOrder(snapshot.data!!)
                    isFirstNotification = false
                }
                else -> logD("$this: $NULL_ORDER_INFO_MESSAGE")
            }
        }
        return Result.retry()
    }

    private fun onNewOrder(data: MutableMap<String, Any>) {
        val orderInfo = data[FIELD_ORDER_INFO] as Map<*, *>
        val orderId = (orderInfo[FIELD_ORDER_ID] as? Long)?.toInt() ?: return
        val guestCount = (orderInfo[FIELD_GUESTS_COUNT] as? Long)?.toInt() ?: return
        readNewOrderUseCase.readNewOrder(
            Order(orderId, guestCount)
        )
//        if (WaiterMainDepsStore.currentEmployee!!.post == COOK)
        if (needToShowNotifications) {
            notificationManager?.notify(
                id++, createNotification(
                    context,
                    context.resources.getString(R.string.newOrderTitle),
                    context.resources.getString(R.string.table, orderId)
                )
            )
        }
    }
}


//TODO: Start a foreground service (maybe)
//class NewOrdersService : Service() {
//
//    private var id = 1
//    private var isFirstNotification = true
//    private var notificationManager: NotificationManager? = null
//    private var readNewOrderUseCase: ReadNewOrderUseCase =
//        CoreDepsStore.appComponent.provideReadNewOrderUseCase()
//
//    companion object {
//        var isRunning = false
//        var ordersListener: ListenerRegistration? = null
//        private val _event: MutableLiveData<ErrorMessageEvent> = MutableLiveData()
//        val event: LiveData<ErrorMessageEvent> = _event
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        notificationManager = createNotificationChannel(this)
////        startForeground(id++, createNotification(this, "NewOrdersService"))
//    }
//
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show()
//        isRunning = true
//        if (ordersListener != null) return START_STICKY
//        ordersListener = newOrdersListenerDocumentRef.addSnapshotListener { snapshot, error ->
//            when {
//                error != null -> {
//                    logE("$this: $error")
//                    return@addSnapshotListener
//                }
//                snapshot != null && snapshot.exists() && snapshot.data != null -> {
//                    if (!isFirstNotification)
//                        onNewOrder(snapshot.data!!)
//                    isFirstNotification = false
//                }
//                else -> logD("$this: $NULL_ORDER_INFO_MESSAGE")
//            }
//        }
//        return START_STICKY
//    }
//
//    override fun onDestroy() {
//        isRunning = false
//        ordersListener?.remove()
//        val intent = Intent("restartservice")
//        sendBroadcast(intent)
//        super.onDestroy()
//    }
//
//    override fun onTaskRemoved(rootIntent: Intent?) {
//        val intent = Intent("YOUR_ACTION_NAME")
//        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
//        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
//        val restartService = Intent(
//            applicationContext,
//            this.javaClass
//        )
//        restartService.setPackage(packageName)
//        val alarmService = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
//        alarmService[AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000] =
//            pendingIntent
//        super.onTaskRemoved(rootIntent)
//    }
//
//    override fun onBind(intent: Intent?): IBinder? = null
//
//    private fun onNewOrder(data: MutableMap<String, Any>) {
////        if (WaiterMainDepsStore.currentEmployee!!.post == COOK)
//        notificationManager?.notify(
//            id++, createNotification(this, data.toString())
//        )
//        val orderInfo = data[FIELD_ORDER_INFO] as Map<*, *>
//        val tableId = (orderInfo[FIELD_ORDER_ID] as Long).toInt()
//        val guestCount = (orderInfo[FIELD_GUESTS_COUNT] as Long).toInt()
//        readNewOrderUseCase.readNewOrder(
//            Order(tableId, guestCount)
//        ) {
//            _event.value = ErrorMessageEvent(it)
//        }
//    }
//}

class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        logD("Service tried to stop")
        Toast.makeText(context, "Restarter", Toast.LENGTH_LONG).show()
        val uploadWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersWorker>(MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MINUTES)
                .build()
        WorkManager.getInstance(context)
            .enqueue(uploadWorkRequest)
    }
}
