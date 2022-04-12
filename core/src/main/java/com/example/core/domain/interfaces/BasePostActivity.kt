package com.example.core.domain.interfaces

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.core.R
import com.example.core.data.workers.NewOrderItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.entities.tools.extensions.Animations.prepareHide
import com.example.core.domain.entities.tools.extensions.Animations.prepareShow
import com.example.core.domain.entities.tools.extensions.Animations.prepareSlideDown
import com.example.core.domain.entities.tools.extensions.Animations.prepareSlideUpFromBottom
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

abstract class BasePostActivity : AppCompatActivity(), EmployeeAuthCallback, LeaveAccountCallback {
    abstract fun setOnNavigationItemSelectedListener()
    fun showNavigationUI(
        rootView: CoordinatorLayout,
        appBar: AppBarLayout,
        bottomNavigationView: BottomNavigationView
    ) {
        val scrollBounds = Rect()
        rootView.getHitRect(scrollBounds)
        if (!appBar.getLocalVisibleRect(scrollBounds))
            appBar.prepareShow(
                appBar.height,
                startDelay = resources.getInteger(R.integer.navigationUIStartDelay).toLong()
            ).start()
        if (!bottomNavigationView.getLocalVisibleRect(scrollBounds))
            bottomNavigationView.prepareSlideUpFromBottom(
                bottomNavigationView.height,
                startDelay = resources.getInteger(R.integer.navigationUIStartDelay).toLong()
            ).start()
    }

    open fun makeWorkerRequests() {
        val newOrdersWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersWorker>(
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MINUTES
            ).build()
        val newOrderItemsStateRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<NewOrderItemStatusWorker>(
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MINUTES
            ).build()
        WorkManager.getInstance(this).also {
            it.enqueueUniquePeriodicWork(
                NewOrdersWorker.NEW_ORDERS_WORKER_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                newOrdersWorkRequest
            )
            it.enqueueUniquePeriodicWork(
                NewOrderItemStatusWorker.NEW_ORDER_ITEM_STATUS_WORKER_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                newOrderItemsStateRequest
            )
        }
    }

    fun hideNavigationUI(
        rootView: CoordinatorLayout,
        appBar: AppBarLayout,
        bottomNavigationView: BottomNavigationView
    ) {
        appBar.prepareHide(appBar.height).start()
        val scrollBounds = Rect()
        rootView.getHitRect(scrollBounds)
        if (appBar.getLocalVisibleRect(scrollBounds))
            appBar.prepareHide(appBar.height).start()
        if (bottomNavigationView.getLocalVisibleRect(scrollBounds))
            bottomNavigationView.prepareSlideDown(bottomNavigationView.height).start()
    }

    open fun observeOnNewPermissionEvent() {}
    open fun observeMenuVersionEvent() {}
}