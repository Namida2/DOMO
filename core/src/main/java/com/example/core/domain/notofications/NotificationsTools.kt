package com.example.core.domain.notofications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.core.R

object NotificationsTools {
    private const val channelId = "0_0"
    fun createNotification(context: Context, title: String, message: String): Notification =
        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_email)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            ).setPriority(NotificationCompat.PRIORITY_HIGH).build()

    fun createNotificationChannel(context: Context): NotificationManager? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.appName)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance)
            return (context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager).also { it.createNotificationChannel(channel) }
        }
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getForegroundNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_email)
            .setColor(context.getColor(android.R.color.transparent))
            .setContentTitle(context.getString(R.string.workWithMenu))
            .setDefaults(NotificationCompat.DEFAULT_SOUND)
            .setAutoCancel(false)
            .build()

    }
}

