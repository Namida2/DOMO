package com.example.cookMain.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cookMain.R
import com.example.cookMain.databinding.ActivityCookMainBinding
import com.example.cookMain.domain.ViewModelFactory
import com.example.cookMain.domain.di.CookMainDepsStore
import com.example.cookMain.domain.di.CookMainDepsStore.deps
import com.example.core.data.workers.NewOrderItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.constants.Messages
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.interfaces.BasePostActivity
import com.example.core.domain.interfaces.OrdersService
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponent
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponentDeps
import com.example.featureCurrentOrders.domain.di.DaggerCurrentOrdersAppComponent
import com.example.featureCurrentOrders.domain.interfaces.OnShowOrderDetailCallback
import com.example.featureLogIn.domain.di.LogInDeps
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureProfile.domain.di.DaggerProfileAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponentDeps
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.example.featureRegistration.presentation.RegistrationFragment
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class CookMainActivity : BasePostActivity(), NavController.OnDestinationChangedListener, OnShowOrderDetailCallback {

    private val currentDestination = 0
    private lateinit var binding: ActivityCookMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val viewModel by viewModels<CookMainViewModel> { ViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideDeps()
        initBinding()
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController.apply {
            addOnDestinationChangedListener(this@CookMainActivity)
        }
        showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        makeWorkerRequests()
        setOnNavigationItemSelectedListener()
    }

    private fun initBinding() {
        binding = ActivityCookMainBinding.inflate(layoutInflater).also {
            it.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun provideDeps() {
        provideCurrentOrdersDeps()
        provideProfileDeps()
    }

    private fun provideCurrentOrdersDeps() {
        val currentOrdersModuleDeps = object : CurrentOrdersAppComponentDeps {
            override val currentEmployee: Employee?
                get() = deps!!.currentEmployee
            override val ordersService: OrdersService
                get() = deps!!.ordersService
        }
        viewModel.currentOrdersAppComponents = DaggerCurrentOrdersAppComponent.builder()
            .provideCurrentOrdersDeps(currentOrdersModuleDeps).build()
        CurrentOrderDepsStore.deps = currentOrdersModuleDeps
        CurrentOrderDepsStore.appComponent = viewModel.currentOrdersAppComponents
        CurrentOrderDepsStore.onShowOrderDetailCallback = this
    }

    private fun provideProfileDeps() {
        val profileModuleDeps = object : ProfileAppComponentDeps {
            override val currentEmployee: Employee?
                get() = deps!!.currentEmployee
            override val firebaseAuth: FirebaseAuth
                get() = deps!!.firebaseAuth
        }
        viewModel.profileAppComponent = DaggerProfileAppComponent.builder()
            .profileAppComponentDeps(profileModuleDeps).build()
        ProfileDepsStore.deps = profileModuleDeps
        ProfileDepsStore.appComponent = viewModel.profileAppComponent
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (destination.id) {
            R.id.currentOrdersFragment -> {
                binding.title.text = resources.getString(R.string.currentOrders)
            }
            R.id.profileFragment -> {
                binding.title.text = resources.getString(R.string.profile)
            }
        }
        binding.bottomNavigation.selectedItemId = destination.id
        showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
    }

    override fun setOnNavigationItemSelectedListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            if (navController.currentDestination?.id == it.itemId)
                return@setOnItemSelectedListener true
            when (it.itemId) {
                R.id.currentOrdersFragment -> {
                    navController.navigate(R.id.navigation_current_orders)
                    true
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> { false }
            }
        }
    }

    override fun observeOnNewPermissionEvent() {
        viewModel.newPermissionEvent.observe(this) {
            it.getData().let {
                createMessageDialog(Messages.newMenuVersionMessage) {
                    finish()
                }?.show(supportFragmentManager, "")
            }
        }
    }

    override fun observeMenuVersionEvent() {
        viewModel.newMenuVersionEvent.observe(this) {
            it.getData()?.let {
                createMessageDialog(Messages.newMenuVersionMessage) {
                    CookMainDepsStore.newMenuVersionCallback?.onNewMenu()
                    viewModelStore.clear()
                }?.show(supportFragmentManager, "")
            }
        }
    }

    override fun onBackPressed() {
        if (navHostFragment.childFragmentManager.fragments[currentDestination] is RegistrationFragment) {
            super.onBackPressed()
            return
        }
        showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        super.onBackPressed()
    }

    override fun onLeaveAccount() {
        hideNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        LogInDepsStore.deps = deps as LogInDeps
        navController.setGraph(R.navigation.navigation_log_in)
    }

    override fun onAuthorisationEvent(employee: Employee?) {
        CookMainDepsStore.employeeAuthCallback?.onAuthorisationEvent(employee)
        viewModelStore.clear()
    }

    override fun makeWorkerRequests() {
        val newOrdersWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersWorker>(
                MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MINUTES
            ).build()
        val newOrderItemsStateRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<NewOrderItemStatusWorker>(
                MIN_PERIODIC_FLEX_MILLIS,
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

    override fun onDestroy() {
        if (!NewOrdersWorker.needToShowNotifications || !NewOrderItemStatusWorker.needToShowNotifications)
            WorkManager.getInstance(this).also {
                it.cancelAllWork()
            }
        else makeWorkerRequests()
        super.onDestroy()
    }

    override fun onShowDetail(orderId: Int) {
        binding.title.text = resources.getString(R.string.order, orderId)
    }

}