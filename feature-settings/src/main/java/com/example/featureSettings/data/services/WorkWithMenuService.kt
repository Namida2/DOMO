package com.example.featureSettings.data.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.notofications.NotificationsTools.createNotification
import com.example.core.domain.notofications.NotificationsTools.createNotificationChannel
import com.example.core.domain.notofications.NotificationsTools.getForegroundNotification
import com.example.featureSettings.R
import com.example.featureSettings.domain.useCases.SaveMenuUseCase
import com.google.firebase.firestore.CollectionReference

class WorkWithMenuService : Service() {

    private var notificationManager: NotificationManager? = null
    private val notificationId = 1

    companion object {
        var simpleTask: SimpleTask? = null
        var targetCollectionRef: CollectionReference? = null
        var saveMenuUseCase: SaveMenuUseCase? = null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = createNotificationChannel(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(notificationId, getForegroundNotification(this))
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        saveMenuUseCase?.saveNewMenu(targetCollectionRef!!, object : SimpleTask {
            override fun onSuccess(result: Unit) {
                simpleTask!!.onSuccess(Unit)
                stopSelf()
            }

            override fun onError(message: ErrorMessage?) {
                simpleTask!!.onError(message)
                stopSelf()
            }

        })
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
