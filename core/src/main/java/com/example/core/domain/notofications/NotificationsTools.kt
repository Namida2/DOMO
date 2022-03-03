package com.example.core.domain.notofications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.core.R

object NotificationsTools {
    private const val channelId = "0_0"
    fun createNotification(context: Context, message: String): Notification =
        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_email)
            .setContentTitle("New order")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH).build()

    fun createNotificationChannel(context: Context): NotificationManager? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = "descriptionText"
            }
            return (context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager).also { it.createNotificationChannel(channel) }
        }
        return null
    }
}