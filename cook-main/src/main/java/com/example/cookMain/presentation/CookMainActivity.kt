package com.example.cookMain.presentation

import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.cookMain.R
import com.example.cookMain.databinding.ActivityCookMainBinding
import com.example.cookMain.domain.di.CookMainDeps
import com.example.cookMain.domain.di.CookMainDepsStore
import com.example.core.data.workers.NewOrdersItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.Employee
import com.example.core.domain.interfaces.BasePostActivity
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.extensions.Animations.prepareHide
import com.example.core.domain.tools.extensions.Animations.prepareShow
import com.example.core.domain.tools.extensions.Animations.prepareSlideDown
import com.example.core.domain.tools.extensions.Animations.prepareSlideUp
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponentDeps
import com.example.featureLogIn.domain.di.LogInDeps
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureProfile.domain.di.ProfileAppComponentDeps
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class CookMainActivity : AppCompatActivity(), BasePostActivity {

    private lateinit var binding: ActivityCookMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCookMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        showNavigationUI()
        provideCurrentOrderDeps()
        provideProfileDeps()
        makeWorkerRequests()
        setOnNavigationItemSelectedListener()
    }

    override fun setOnNavigationItemSelectedListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val currentFragment = navHostFragment.parentFragmentManager.fragments[0]
            when (it.itemId) {
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

    private fun provideProfileDeps() {
        ProfileDepsStore.deps = object : ProfileAppComponentDeps {
            override val currentEmployee: Employee?
                get() = CookMainDepsStore.deps.currentEmployee
            override val firebaseAuth: FirebaseAuth
                get() = CookMainDepsStore.deps.firebaseAuth
        }
    }

    private fun provideCurrentOrderDeps() {
        CurrentOrderDepsStore.deps = object : CurrentOrdersAppComponentDeps {
            override val currentEmployee: Employee?
                get() = CookMainDepsStore.deps.currentEmployee
            override val ordersService: OrdersService<OrdersServiceSub>
                get() = CookMainDepsStore.deps.ordersService
        }
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
        LogInDepsStore.deps = CookMainDepsStore.deps as LogInDeps
        navController.setGraph(R.navigation.navigation_log_in)
    }

    override fun onEmployeeLoggedIn(employee: Employee?) {
        CookMainDepsStore.employeeAuthCallback.onEmployeeLoggedIn(employee)
    }
}