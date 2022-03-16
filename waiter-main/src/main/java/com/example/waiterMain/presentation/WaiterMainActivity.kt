package com.example.waiterMain.presentation

import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.work.PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.core.data.workers.NewOrdersItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.Employee
import com.example.core.domain.interfaces.BasePostActivity
import com.example.core.domain.tools.extensions.Animations.prepareHide
import com.example.core.domain.tools.extensions.Animations.prepareShow
import com.example.core.domain.tools.extensions.Animations.prepareSlideDown
import com.example.core.domain.tools.extensions.Animations.prepareSlideUp
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.featureLogIn.domain.di.LogInDeps
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureOrder.presentation.order.OrderFragment
import com.example.featureOrder.presentation.tables.TablesFragment
import com.example.waiterMain.R
import com.example.waiterMain.databinding.ActivityWaiterMainBinding
import com.example.waiterMain.domain.di.WaiterMainDepsStore
import java.util.concurrent.TimeUnit

class WaiterMainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener, BasePostActivity {

    private val currentDestination = 0
    private lateinit var binding: ActivityWaiterMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaiterMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController.apply {
            addOnDestinationChangedListener(this@WaiterMainActivity)
        }
        showNavigationUI()
        setOnNavigationItemSelectedListener()
        makeWorkerRequests()
    }


    override fun makeWorkerRequests() {
        val newOrdersWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersWorker>(
                MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MINUTES
            ).build()
        val newOrderItemsStateRequest: WorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersItemStatusWorker>(
                MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MINUTES
            ).build()
        //TODO: Workers not start after leaving account
        WorkManager.getInstance(this).enqueue(newOrdersWorkRequest)
        WorkManager.getInstance(this).enqueue(newOrderItemsStateRequest)
        observeOrdersWorkerEvents()
    }

    private fun observeOrdersWorkerEvents() {
        NewOrdersWorker.event.observe(this) {
            val data = it.getData() ?: return@observe
            createMessageDialog(data)?.show(supportFragmentManager, "")
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (destination.id) {
            R.id.orderFragment -> {
                hideNavigationUI()
            }
            R.id.tablesFragment -> {
                showNavigationUI()
            }
            R.id.profileFragment -> {
                showNavigationUI()
            }
        }
    }

    override fun setOnNavigationItemSelectedListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val currentFragment = navHostFragment.parentFragmentManager.fragments[0]
            when (it.itemId) {
                R.id.tablesFragment -> {
                    navController.navigate(R.id.navigation_order)
                    true
                }
                R.id.currentOrdersFragment -> {
                    navController.navigate(R.id.navigation_current_orders)
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
        when (navHostFragment.childFragmentManager.fragments[currentDestination]) {
            is OrderFragment -> {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    TablesFragment.isReturnedFromOrderFragment,
                    true
                )
            }
        }
        showNavigationUI()
        super.onBackPressed()
    }

    override fun onLeaveAccount() {
        hideNavigationUI()
        LogInDepsStore.deps = WaiterMainDepsStore.profileDeps as LogInDeps
        navController.setGraph(R.navigation.navigation_log_in)
    }

    override fun onEmployeeLoggedIn(employee: Employee?) {
        WaiterMainDepsStore.employeeAuthCallback.onEmployeeLoggedIn(employee)
    }
}

