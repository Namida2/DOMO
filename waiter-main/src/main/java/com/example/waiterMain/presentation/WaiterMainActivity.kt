package com.example.waiterMain.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.work.PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.core.data.workers.NewOrdersItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.entities.Settings
import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.BasePostActivity
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.entities.order.OrdersServiceSub
import com.example.core.domain.entities.tools.constants.ErrorMessages.permissionDeniedMessage
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponent
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponentDeps
import com.example.featureCurrentOrders.domain.di.DaggerCurrentOrdersAppComponent
import com.example.featureLogIn.domain.di.LogInDeps
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureOrder.domain.di.DaggerOrderAppComponent
import com.example.featureOrder.domain.di.OrderAppComponent
import com.example.featureOrder.domain.di.OrderAppComponentDeps
import com.example.featureOrder.domain.di.OrderDepsStore
import com.example.featureOrder.presentation.order.OrderFragment
import com.example.featureOrder.presentation.tables.TablesFragment
import com.example.featureProfile.domain.di.DaggerProfileAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponentDeps
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.example.waiterMain.R
import com.example.waiterMain.databinding.ActivityWaiterMainBinding
import com.example.waiterMain.domain.di.WaiterMainDepsStore
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class WaiterMainActivity : BasePostActivity(),
    NavController.OnDestinationChangedListener {

    private val currentDestination = 0
    private lateinit var profileAppComponent: ProfileAppComponent
    private lateinit var orderAppComponents: OrderAppComponent
    private lateinit var currentOrdersAppComponents: CurrentOrdersAppComponent
    private lateinit var binding: ActivityWaiterMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private val viewModel by viewModels<WaiterMainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideDeps()
        binding = ActivityWaiterMainBinding.inflate(layoutInflater)
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
    }

    private fun provideDeps() {
        provideOrderDeps()
        provideCurrentOrdersDeps()
        provideProfileDeps()
    }


    private fun provideOrderDeps() {
        val orderModuleDeps = object : OrderAppComponentDeps {
            override val settings: Settings
                get() = WaiterMainDepsStore.deps.settings
            override val currentEmployee: Employee?
                get() = WaiterMainDepsStore.deps.currentEmployee
            override val ordersService: OrdersService
                get() = WaiterMainDepsStore.deps.ordersService
        }
        orderAppComponents =
            DaggerOrderAppComponent.builder().provideOrderAppComponentDeps(orderModuleDeps).build()
        OrderDepsStore.deps = orderModuleDeps
        OrderDepsStore.appComponent = orderAppComponents
    }

    private fun provideCurrentOrdersDeps() {
        val currentOrdersModuleDeps = object : CurrentOrdersAppComponentDeps {
            override val currentEmployee: Employee?
                get() = WaiterMainDepsStore.deps.currentEmployee
            override val ordersService: OrdersService
                get() = WaiterMainDepsStore.deps.ordersService
        }
        currentOrdersAppComponents = DaggerCurrentOrdersAppComponent.builder()
            .provideCurrentOrdersDeps(currentOrdersModuleDeps).build()
        CurrentOrderDepsStore.deps = currentOrdersModuleDeps
        CurrentOrderDepsStore.appComponent = currentOrdersAppComponents
    }

    private fun provideProfileDeps() {
        val profileModuleDeps = object : ProfileAppComponentDeps {
            override val currentEmployee: Employee?
                get() = WaiterMainDepsStore.deps.currentEmployee
            override val firebaseAuth: FirebaseAuth
                get() = WaiterMainDepsStore.profileDeps.firebaseAuth
        }
        profileAppComponent = DaggerProfileAppComponent.builder()
            .profileAppComponentDeps(profileModuleDeps).build()
        ProfileDepsStore.deps = profileModuleDeps
        ProfileDepsStore.appComponent = profileAppComponent
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

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (destination.id) {
            R.id.orderFragment -> {
                hideNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
            }
            R.id.tablesFragment -> {
                showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
            }
            R.id.profileFragment -> {
                showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
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

    override fun onBackPressed() {
        when (navHostFragment.childFragmentManager.fragments[currentDestination]) {
            is OrderFragment -> {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    TablesFragment.isReturnedFromOrderFragment,
                    true
                )
            }
        }
        showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        super.onBackPressed()
    }

    override fun onLeaveAccount() {
        hideNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        LogInDepsStore.deps = WaiterMainDepsStore.profileDeps as LogInDeps
        navController.setGraph(R.navigation.navigation_log_in)
    }

    override fun onEmployeeLoggedIn(employee: Employee?) {
        WaiterMainDepsStore.employeeAuthCallback.onEmployeeLoggedIn(employee)
    }

    override fun observeOnNewPermissionEvent() {
        viewModel.newPermissionEvent.observe(this) {
            it.getData().let {
                createMessageDialog(permissionDeniedMessage) {
                    finish()
                }?.show(supportFragmentManager, "")
            }
        }
    }

    private fun observeOrdersWorkerEvents() {
        NewOrdersWorker.event.observe(this) {
            val data = it.getData() ?: return@observe
            createMessageDialog(data)?.show(supportFragmentManager, "")
        }
    }
}

