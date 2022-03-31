package com.example.core.domain.interfaces

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.core.R
import com.example.core.domain.entities.tools.extensions.Animations.prepareHide
import com.example.core.domain.entities.tools.extensions.Animations.prepareShow
import com.example.core.domain.entities.tools.extensions.Animations.prepareSlideDown
import com.example.core.domain.entities.tools.extensions.Animations.prepareSlideUp
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BasePostActivity : AppCompatActivity(), EmployeeAuthCallback, LeaveAccountCallback {
    abstract fun setOnNavigationItemSelectedListener()
    abstract fun makeWorkerRequests()
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
            bottomNavigationView.prepareSlideUp(
                bottomNavigationView.height,
                startDelay = resources.getInteger(R.integer.navigationUIStartDelay).toLong()
            ).start()
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