package com.example.administratorMain.presentation

import android.os.Bundle
import androidx.activity.viewModels
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
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.constants.ErrorMessages
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.featureEmployees.domain.di.DaggerEmployeesAppComponent
import com.example.featureEmployees.domain.di.EmployeesAppComponent
import com.example.featureEmployees.domain.di.EmployeesAppComponentDeps
import com.example.featureEmployees.domain.di.EmployeesDepsStore
import com.example.featureLogIn.domain.di.LogInDeps
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureSettings.domain.SettingsDeps
import com.example.featureSettings.domain.SettingsDepsStore
import java.util.concurrent.TimeUnit

class AdministratorMainActivity : BasePostActivity() {

    private lateinit var employeesAppComponent: EmployeesAppComponent

    private lateinit var binding: ActivityAdministratorBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private val viewModel by viewModels<AdminViewMode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideDeps()
        initialise()
    }

    private fun initialise() {
        binding = ActivityAdministratorBinding.inflate(layoutInflater)
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        makeWorkerRequests()
        setOnNavigationItemSelectedListener()
        setContentView(binding.root)
    }

    private fun provideDeps() {
        provideEmployeeAppComponent()
        provideSettingsDeps()
    }

    private fun provideEmployeeAppComponent() {
        val employeesModuleDeps = object : EmployeesAppComponentDeps {
            override val currentEmployee: Employee?
                get() = AdminDepsStore.deps.currentEmployee
        }
        employeesAppComponent = DaggerEmployeesAppComponent.builder()
            .employeesAppComponentDeps(employeesModuleDeps).build()
        EmployeesDepsStore.deps = employeesModuleDeps
        EmployeesDepsStore.appComponent = employeesAppComponent
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
        LogInDepsStore.deps = AdminDepsStore.deps as LogInDeps
        navController.setGraph(R.navigation.navigation_log_in)
    }

    override fun onEmployeeLoggedIn(employee: Employee?) {
        AdminDepsStore.employeeAuthCallback.onEmployeeLoggedIn(employee)
    }

    private fun provideSettingsDeps() {
        SettingsDepsStore.deps = object : SettingsDeps {
            override val currentEmployee: Employee?
                get() = AdminDepsStore.deps.currentEmployee
            override val ordersService: OrdersService<OrdersServiceSub>
                get() = AdminDepsStore.deps.ordersService
        }
    }
}