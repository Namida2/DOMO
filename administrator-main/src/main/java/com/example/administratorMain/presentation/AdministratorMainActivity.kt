package com.example.administratorMain.presentation

import android.graphics.Rect
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.administratorMain.R
import com.example.administratorMain.databinding.ActivityAdministratorBinding
import com.example.administratorMain.domatn.di.AdminDepsStore
import com.example.core.data.workers.NewOrdersItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.BasePostActivity
import com.example.core.domain.tools.constants.ErrorMessages
import com.example.core.domain.tools.extensions.Animations.prepareHide
import com.example.core.domain.tools.extensions.Animations.prepareShow
import com.example.core.domain.tools.extensions.Animations.prepareSlideDown
import com.example.core.domain.tools.extensions.Animations.prepareSlideUp
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.featureLogIn.domain.di.LogInDeps
import com.example.featureLogIn.domain.di.LogInDepsStore
import java.util.concurrent.TimeUnit

class AdministratorMainActivity: AppCompatActivity(), BasePostActivity {
    private lateinit var binding: ActivityAdministratorBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private val viewModel by viewModels<AdminViewMode> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdministratorBinding.inflate(layoutInflater)
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        setOnNavigationItemSelectedListener()
        makeWorkerRequests()
        showNavigationUI()
        setContentView(binding.root)
    }

    override fun setOnNavigationItemSelectedListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val currentFragment = navHostFragment.parentFragmentManager.fragments[0]
            when (it.itemId) {
                R.id.employeesFragment -> {
                    navController.navigate(R.id.employeesFragment)
                    true
                }
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun makeWorkerRequests() {
        val newOrdersWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersWorker>(
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MINUTES
            ).build()
        val newOrderItemsStateRequest: WorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersItemStatusWorker>(
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MINUTES
            ).build()
        WorkManager.getInstance(this).enqueue(newOrdersWorkRequest)
        WorkManager.getInstance(this).enqueue(newOrderItemsStateRequest)
    }

    override fun hideNavigationUI() {
        with(binding) {
            appBar.prepareHide(appBar.height).start()
            val scrollBounds = Rect()
            rootCoordinationLayout.getHitRect(scrollBounds)
            if (appBar.getLocalVisibleRect(scrollBounds))
                appBar.prepareHide(appBar.height).start()
            if (bottomNavigation.getLocalVisibleRect(scrollBounds))
                bottomNavigation.prepareSlideDown(bottomNavigation.height).start()
        }
    }

    override fun observeOnNewPermissionEvent() {
        viewModel.newPermissionEvent.observe(this) {
            it.getData().let {
                createMessageDialog(ErrorMessages.permissionDeniedMessage) {
                    finish()
                }?.show(supportFragmentManager, "")
            }
        }

    }

    override fun showNavigationUI() {
        with(binding) {
            val scrollBounds = Rect()
            rootCoordinationLayout.getHitRect(scrollBounds)
            if (!appBar.getLocalVisibleRect(scrollBounds))
                appBar.prepareShow(
                    appBar.height,
                    startDelay = resources.getInteger(R.integer.navigationUIStartDelay).toLong()
                ).start()
            if (!bottomNavigation.getLocalVisibleRect(scrollBounds))
                bottomNavigation.prepareSlideUp(
                    bottomNavigation.height,
                    startDelay = resources.getInteger(R.integer.navigationUIStartDelay).toLong()
                ).start()
        }
    }

    override fun onBackPressed() {
        showNavigationUI()
        super.onBackPressed()
    }

    override fun onLeaveAccount() {
        hideNavigationUI()
        LogInDepsStore.deps = AdminDepsStore.deps as LogInDeps
        navController.setGraph(R.navigation.navigation_log_in)
    }

    override fun onEmployeeLoggedIn(employee: Employee?) {
        AdminDepsStore.employeeAuthCallback.onEmployeeLoggedIn(employee)
    }

}