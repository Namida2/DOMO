package com.example.waiterMain.presentation

import android.content.Intent
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
import com.example.core.data.workers.Restarter
import com.example.core.domain.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.extensions.Animations.prepareHide
import com.example.core.domain.tools.extensions.Animations.prepareShow
import com.example.core.domain.tools.extensions.Animations.prepareSlideDown
import com.example.core.domain.tools.extensions.Animations.prepareSlideUp
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponentDeps
import com.example.featureOrder.domain.di.OrderAppComponentDeps
import com.example.featureOrder.domain.di.OrderDepsStore
import com.example.featureOrder.presentation.order.OrderFragment
import com.example.featureOrder.presentation.tables.TablesFragment
import com.example.waiterMain.R
import com.example.waiterMain.databinding.ActivityWaiterMainBinding
import com.example.waiterMain.domain.di.WaiterMainDepsStore
import java.util.concurrent.TimeUnit

class WaiterMainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private val currentDestination = 0
    private lateinit var binding: ActivityWaiterMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var wasNavigatedToOrderFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaiterMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController.apply {
            addOnDestinationChangedListener(this@WaiterMainActivity)
        }
        setOnItemSelectedListener()
        provideFeatureOrderDeps()
        provideCurrentOrderDeps()
        makeWorkerRequests()
    }

    private fun makeWorkerRequests() {
        val newOrdersWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersWorker>(MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MINUTES).build()
        val newOrderItemsStateRequest: WorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersItemStatusWorker>(MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(newOrdersWorkRequest)
        WorkManager.getInstance(this).enqueue(newOrderItemsStateRequest)
        observeOrdersWorkerEvents()
    }

    override fun onDestroy() {
//        val broadcastIntent = Intent()
//        broadcastIntent.action = "restartservice"
//        broadcastIntent.setClass(this, Restarter::class.java)
//        this.sendBroadcast(broadcastIntent)
        super.onDestroy()
    }

    private fun observeOrdersWorkerEvents() {
        NewOrdersWorker.event.observe(this) {
            val data = it.getData() ?: return@observe
            createMessageDialog(data)?.show(supportFragmentManager, "")
        }
    }

    private fun provideFeatureOrderDeps() {
        OrderDepsStore.deps = object : OrderAppComponentDeps {
            override val ordersService: OrdersService<OrdersServiceSub>
                get() = WaiterMainDepsStore.deps.ordersService
        }
    }

    private fun provideCurrentOrderDeps() {
        CurrentOrderDepsStore.deps = object : CurrentOrdersAppComponentDeps {
            override val currentEmployee: Employee?
                get() = WaiterMainDepsStore.deps.currentEmployee
            override val ordersService: OrdersService<OrdersServiceSub>
                get() = WaiterMainDepsStore.deps.ordersService
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
                wasNavigatedToOrderFragment = true
            }
            R.id.tablesFragment -> {
                showNavigationUI()
                wasNavigatedToOrderFragment = false
            }
        }
    }

    private fun setOnItemSelectedListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            var currentFragment = navHostFragment.parentFragmentManager.fragments[0]
            when (it.itemId) {
                R.id.tablesFragment -> {
                    navController.navigate(R.id.navigation_order)
                    true
                }
                R.id.currentOrdersFragment -> {
                    navController.navigate(R.id.navigation_current_orders)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun hideNavigationUI() {
        with(binding) {
            appBar.prepareHide(appBar.height).start()
            val scrollBounds = Rect()
            rootCoordinationLayout.getHitRect(scrollBounds)
            if (bottomNavigation.getLocalVisibleRect(scrollBounds)) {
                bottomNavigation.prepareSlideDown(bottomNavigation.height).start()
            }
        }
    }

    private fun showNavigationUI() {
        if (!wasNavigatedToOrderFragment) return
        with(binding) {
            appBar.prepareShow(appBar.height).start()
            bottomNavigation.prepareSlideUp(bottomNavigation.height, startDelay = 150).start()
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
        super.onBackPressed()
    }
}

