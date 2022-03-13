package com.example.cookMain.presentation

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
import com.example.cookMain.domain.di.CookMainDepsStore
import com.example.core.data.workers.NewOrdersItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponentDeps
import com.example.featureProfile.domain.di.ProfileAppComponentDeps
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class CookMainActivity : AppCompatActivity() {

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
        provideCurrentOrderDeps()
        provideProfileDeps()
        makeWorkerRequests()
        setOnItemSelectedListener()
    }

    private fun setOnItemSelectedListener() {
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

    private fun makeWorkerRequests() {
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
        observeOrdersWorkerEvents()
    }

    private fun observeOrdersWorkerEvents() {
        NewOrdersWorker.event.observe(this) {
            val data = it.getData() ?: return@observe
            createMessageDialog(data)?.show(supportFragmentManager, "")
        }
    }
}