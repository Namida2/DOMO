package com.example.waiterMain.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.core.data.workers.NewOrderItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.Settings
import com.example.core.domain.entities.tools.constants.Messages
import com.example.core.domain.entities.tools.constants.Messages.newMenuVersionMessage
import com.example.core.domain.entities.tools.constants.Messages.permissionDeniedMessage
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.interfaces.BasePostActivity
import com.example.core.domain.interfaces.OrdersService
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponentDeps
import com.example.featureCurrentOrders.domain.di.DaggerCurrentOrdersAppComponent
import com.example.featureCurrentOrders.domain.interfaces.OnShowOrderDetailCallback
import com.example.featureLogIn.domain.di.LogInDeps
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureOrder.domain.di.DaggerOrderAppComponent
import com.example.featureOrder.domain.di.OrderAppComponentDeps
import com.example.featureOrder.domain.di.OrderDepsStore
import com.example.featureOrder.presentation.order.OrderFragment
import com.example.featureOrder.presentation.tables.TablesFragment
import com.example.featureProfile.domain.di.DaggerProfileAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponentDeps
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.example.featureRegistration.presentation.RegistrationFragment
import com.example.waiterMain.R
import com.example.waiterMain.databinding.ActivityWaiterMainBinding
import com.example.waiterMain.domain.ViewModelFactory
import com.example.waiterMain.domain.di.WaiterMainDepsStore
import com.example.waiterMain.domain.di.WaiterMainDepsStore.deps
import com.example.waiterMain.domain.di.WaiterMainDepsStore.profileDeps
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class WaiterMainActivity : BasePostActivity(),
    NavController.OnDestinationChangedListener, OnShowOrderDetailCallback {

    private val currentDestination = 0
    private lateinit var binding: ActivityWaiterMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private val viewModel by viewModels<WaiterMainViewModel> { ViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideDeps()
        initBinding()
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController.apply {
            addOnDestinationChangedListener(this@WaiterMainActivity)
        }
        showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        setOnNavigationItemSelectedListener()
        makeWorkerRequests()
        observeOnNewPermissionEvent()
        observeMenuVersionEvent()
    }

    private fun initBinding() {
        binding = ActivityWaiterMainBinding.inflate(layoutInflater).also {
            it.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun provideDeps() {
        provideOrderDeps()
        provideCurrentOrdersDeps()
        provideProfileDeps()
    }

    private fun provideOrderDeps() {
        val orderModuleDeps = object : OrderAppComponentDeps {
            override val settings: Settings
                get() = deps!!.settings
            override val currentEmployee: Employee?
                get() = deps!!.currentEmployee
            override val ordersService: OrdersService
                get() = deps!!.ordersService
        }
        viewModel.orderAppComponents =
            DaggerOrderAppComponent.builder().provideOrderAppComponentDeps(orderModuleDeps).build()
        OrderDepsStore.deps = orderModuleDeps
        OrderDepsStore.appComponent = viewModel.orderAppComponents
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
                get() = profileDeps!!.firebaseAuth
        }
        viewModel.profileAppComponent = DaggerProfileAppComponent.builder()
            .profileAppComponentDeps(profileModuleDeps).build()
        ProfileDepsStore.deps = profileModuleDeps
        ProfileDepsStore.appComponent = viewModel.profileAppComponent
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

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (destination.id) {
            R.id.orderFragment -> {
                hideNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
            }
            R.id.currentOrdersFragment -> {
                showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
                binding.title.text = resources.getString(R.string.currentOrders)
            }
            R.id.tablesFragment -> {
                showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
                binding.title.text = resources.getString(R.string.tables)
            }
            R.id.profileFragment -> {
                showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
                binding.title.text = resources.getString(R.string.profile)
            }
        }
        binding.bottomNavigation.selectedItemId = destination.id
    }

    override fun setOnNavigationItemSelectedListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            if (navController.currentDestination?.id == it.itemId)
                return@setOnItemSelectedListener true
            when (it.itemId) {
                R.id.tablesFragment -> {
                    navController.navigate(R.id.navigation_order); true
                }
                R.id.currentOrdersFragment -> {
                    navController.navigate(R.id.navigation_current_orders); true
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment); true
                }
                else -> {
                    false
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onBackPressed() {
        when (navHostFragment.childFragmentManager.fragments[currentDestination]) {
            is OrderFragment -> {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    TablesFragment.isReturnedFromOrderFragment, true
                )
            }
            is RegistrationFragment -> {
                super.onBackPressed(); return
            }
        }
        showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        super.onBackPressed()
    }

    override fun onLeaveAccount() {
        hideNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        LogInDepsStore.deps = profileDeps as LogInDeps
        navController.setGraph(R.navigation.navigation_log_in)
    }

    override fun onAuthorisationEvent(employee: Employee?) {
        WaiterMainDepsStore.employeeAuthCallback?.onAuthorisationEvent(employee)
        viewModelStore.clear()
    }

    override fun observeOnNewPermissionEvent() {
        viewModel.newPermissionEvent.observe(this) {
            it.getData()?.let {
                createMessageDialog(permissionDeniedMessage) {
                    finish()
                }?.show(supportFragmentManager, "")
            }
        }
    }

    override fun observeMenuVersionEvent() {
        viewModel.newMenuVersionEvent.observe(this) {
            it.getData()?.let {
                createMessageDialog(newMenuVersionMessage) {
                    WaiterMainDepsStore.newMenuVersionCallback?.onNewMenu()
                    viewModelStore.clear()
                }?.show(supportFragmentManager, "")
            }
        }
    }

    override fun onShowDetail(orderId: Int) {
        binding.title.text = resources.getString(R.string.order, orderId)
    }

}

