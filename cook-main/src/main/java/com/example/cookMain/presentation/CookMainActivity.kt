package com.example.cookMain.presentation

import android.os.Bundle
import androidx.activity.viewModels
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
import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.BasePostActivity
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.entities.order.OrdersServiceSub
import com.example.core.domain.entities.tools.constants.ErrorMessages
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponent
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponentDeps
import com.example.featureCurrentOrders.domain.di.DaggerCurrentOrdersAppComponent
import com.example.featureLogIn.domain.di.LogInDeps
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureProfile.domain.di.DaggerProfileAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponentDeps
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class CookMainActivity : BasePostActivity() {

    private lateinit var currentOrdersAppComponents: CurrentOrdersAppComponent
    private lateinit var profileAppComponent: ProfileAppComponent
    private lateinit var binding: ActivityCookMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val viewModel by viewModels<CookViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideDeps()
        binding = ActivityCookMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        makeWorkerRequests()
        setOnNavigationItemSelectedListener()
    }

    private fun provideDeps() {
        provideCurrentOrdersDeps()
        provideProfileDeps()
    }

    private fun provideCurrentOrdersDeps() {
        val currentOrdersModuleDeps = object : CurrentOrdersAppComponentDeps {
            override val currentEmployee: Employee?
                get() = CookMainDepsStore.deps.currentEmployee
            override val ordersService: OrdersService
                get() = CookMainDepsStore.deps.ordersService
        }
        currentOrdersAppComponents = DaggerCurrentOrdersAppComponent.builder()
            .provideCurrentOrdersDeps(currentOrdersModuleDeps).build()
        CurrentOrderDepsStore.deps = currentOrdersModuleDeps
        CurrentOrderDepsStore.appComponent = currentOrdersAppComponents
    }

    private fun provideProfileDeps() {
        val profileModuleDeps = object : ProfileAppComponentDeps {
            override val currentEmployee: Employee?
                get() = CookMainDepsStore.deps.currentEmployee
            override val firebaseAuth: FirebaseAuth
                get() = CookMainDepsStore.deps.firebaseAuth
        }
        profileAppComponent = DaggerProfileAppComponent.builder()
            .profileAppComponentDeps(profileModuleDeps).build()
        ProfileDepsStore.deps = profileModuleDeps
        ProfileDepsStore.appComponent = profileAppComponent
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

    override fun observeOnNewPermissionEvent() {
        viewModel.newPermissionEvent.observe(this) {
            it.getData().let {
                createMessageDialog(ErrorMessages.permissionDeniedMessage) {
                    finish()
                }?.show(supportFragmentManager, "")
            }
        }
    }

    override fun onBackPressed() {
        showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        super.onBackPressed()
    }

    override fun onLeaveAccount() {
        hideNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        LogInDepsStore.deps = CookMainDepsStore.deps as LogInDeps
        navController.setGraph(R.navigation.navigation_log_in)
    }

    override fun onEmployeeLoggedIn(employee: Employee?) {
        CookMainDepsStore.employeeAuthCallback.onEmployeeLoggedIn(employee)
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
}