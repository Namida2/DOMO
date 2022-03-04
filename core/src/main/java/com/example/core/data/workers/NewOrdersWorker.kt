package com.example.core.data.workers

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import androidx.work.WorkRequest.MIN_BACKOFF_MILLIS
import com.example.core.domain.di.CoreDepsStore
import com.example.core.domain.notofications.NotificationsTools.createNotification
import com.example.core.domain.notofications.NotificationsTools.createNotificationChannel
import com.example.core.domain.order.Order
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.Event
import com.example.core.domain.tools.FirestoreReferences.newOrdersListenerDocumentRef
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_GUESTS_COUNT
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_ID
import com.example.core.domain.tools.constants.FirestoreConstants.FIELD_ORDER_INFO
import com.example.core.domain.tools.constants.OtherStringConstants.NULL_ORDER_INFO_MESSAGE
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import com.example.core.domain.useCases.ReadNewOrderUseCase
import com.google.firebase.firestore.ListenerRegistration
import java.util.concurrent.TimeUnit

typealias ErrorMessageEvent = Event<ErrorMessage>

class NewOrdersWorker(
    private val context: Context, params: WorkerParameters
) : CoroutineWorker(context, params) {

    private var id = 0
    private var notificationManager: NotificationManager? = null
    private var readNewOrderUseCase: ReadNewOrderUseCase =
        CoreDepsStore.appComponent.provideReadNewOrderUseCase()

    companion object {
        var isFirstNotification = true
        var ordersListener: ListenerRegistration? = null
        private val _event: MutableLiveData<ErrorMessageEvent> = MutableLiveData()
        val event: LiveData<ErrorMessageEvent> = _event
    }

    override suspend fun doWork(): Result {
        if (ordersListener != null) return Result.retry()
        notificationManager = createNotificationChannel(context)
        ordersListener = newOrdersListenerDocumentRef.addSnapshotListener { snapshot, error ->
            when {
                error != null -> {
                    logE("$this: $error")
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
        return Result.success()
    }

    private fun onNewOrder(data: MutableMap<String, Any>) {
//        if (WaiterMainDepsStore.currentEmployee!!.post == COOK)
        notificationManager?.notify(
            id++, createNotification(context, data.toString())
        )
        val orderInfo = data[FIELD_ORDER_INFO] as Map<*, *>
        val tableId = (orderInfo[FIELD_ORDER_ID] as Long).toInt()
        val guestCount = (orderInfo[FIELD_GUESTS_COUNT] as Long).toInt()
        readNewOrderUseCase.readNewOrder(
            Order(tableId, guestCount)
        ) {
            _event.value = ErrorMessageEvent(it)
        }
    }


}

//TODO: Start a foreground service
class NewOrdersService(): Service() {

    private var id = 0
    private var isFirstNotification = true
    private var notificationManager: NotificationManager? = null
    private var readNewOrderUseCase: ReadNewOrderUseCase =
        CoreDepsStore.appComponent.provideReadNewOrderUseCase()

    var ordersListener: ListenerRegistration? = null
    private val _event: MutableLiveData<ErrorMessageEvent> = MutableLiveData()
    val event: LiveData<ErrorMessageEvent> = _event

    companion object {
        var isRunning = false
    }

    override fun onCreate() {

        super.onCreate()
    }

    //TODO: Don't show the notification when it receive the fist notification
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        if (ordersListener != null) return START_STICKY
        notificationManager = createNotificationChannel(this)
        ordersListener = newOrdersListenerDocumentRef.addSnapshotListener { snapshot, error ->
            when {
                error != null -> {
                    logE("$this: $error")
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
        return START_STICKY
    }

    private fun onNewOrder(data: MutableMap<String, Any>) {
//        if (WaiterMainDepsStore.currentEmployee!!.post == COOK)
        notificationManager?.notify(
            id++, createNotification(this, data.toString())
        )
        val orderInfo = data[FIELD_ORDER_INFO] as Map<*, *>
        val tableId = (orderInfo[FIELD_ORDER_ID] as Long).toInt()
        val guestCount = (orderInfo[FIELD_GUESTS_COUNT] as Long).toInt()
        readNewOrderUseCase.readNewOrder(
            Order(tableId, guestCount)
        ) {
            _event.value = ErrorMessageEvent(it)
        }
    }

    override fun onDestroy() {
        isRunning = false
        ordersListener?.remove()
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        val restartServicePendingIntent = PendingIntent.getService(
            applicationContext,
            1,
            restartServiceIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val alarmService = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmService[AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000] =
            restartServicePendingIntent
        super.onTaskRemoved(rootIntent)
    }

    override fun onBind(intent: Intent?): IBinder? = null
    class Restarter : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            logD("Service tried to stop")

            val uploadWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersWorker>(MIN_BACKOFF_MILLIS, TimeUnit.MINUTES)
                .build()
        WorkManager.getInstance(context)
            .enqueue(uploadWorkRequest)
//        observeOrdersWorkerEvents()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Toast.makeText(context, "ForegroundService", Toast.LENGTH_SHORT).show()
//                context.startForegroundService(Intent(context, NewOrdersService::class.java))
//            } else {
//                Toast.makeText(context, "StartService", Toast.LENGTH_SHORT).show()
//                context.startService(Intent(context, NewOrdersService::class.java))
//            }
        }
    }
}