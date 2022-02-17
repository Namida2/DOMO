package com.example.waiterMain.domain

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.waiterCore.domain.tools.FirestoreReferences.newOrdersListenerDocumentRef
import com.example.waiterCore.domain.tools.extensions.logD
import com.example.waiterCore.domain.tools.extensions.logE
import com.example.waiterMain.R
import com.google.firebase.firestore.ListenerRegistration

class MyWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    companion object {
        var ordersListener: ListenerRegistration? = null
    }

    private val CHANNEL_ID = "0_0"
    private var id = 0
    private lateinit var notificationManager: NotificationManager
    override suspend fun doWork(): Result {
        if (ordersListener != null) return Result.retry()
        createNotificationChannel()
        ordersListener = newOrdersListenerDocumentRef.addSnapshotListener { snapshot, error ->
            if (null != error) {
                logE("Listen failed: $error")
                return@addSnapshotListener
            }

            //TODO: Read order

            if (snapshot != null && snapshot.exists()) {
                logD("Current data: ${snapshot.data}")
                notificationManager.notify(
                    id++,
                    createNotification(snapshot.data.toString())
                )
            } else {
                logD("Current data: null")
            }
        }
        return Result.success()
    }


    private fun createNotification(message: String): Notification {
        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_email)
            .setContentTitle("New order")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        return builder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = "descriptionText"
            }
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}