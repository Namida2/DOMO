package com.example.administratorMain.presentation

import android.content.SharedPreferences
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
import com.example.administratorMain.domatn.di.AdminDepsStore.deps
import com.example.core.data.database.Database
import com.example.core.data.workers.NewOrdersItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.Settings
import com.example.core.domain.interfaces.BasePostActivity
import com.example.core.domain.interfaces.OrdersService
import com.example.featureEmployees.domain.di.DaggerEmployeesAppComponent
import com.example.featureEmployees.domain.di.EmployeesAppComponent
import com.example.featureEmployees.domain.di.EmployeesAppComponentDeps
import com.example.featureEmployees.domain.di.EmployeesDepsStore
import com.example.featureLogIn.domain.di.LogInDeps
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureProfile.domain.di.DaggerProfileAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponentDeps
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.example.featureSettings.domain.di.DaggerSettingsAppComponent
import com.example.featureSettings.domain.di.SettingsAppComponent
import com.example.featureSettings.domain.di.SettingsAppComponentDeps
import com.example.featureSettings.domain.di.SettingsDepsStore
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class AdministratorMainActivity : BasePostActivity() {

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
        provideProfileDeps()
    }

    private fun provideEmployeeAppComponent() {
        val employeesModuleDeps = object : EmployeesAppComponentDeps {
            override val currentEmployee: Employee?
                get() = deps!!.currentEmployee
        }
        viewModel.employeesAppComponent = DaggerEmployeesAppComponent.builder()
            .employeesAppComponentDeps(employeesModuleDeps).build()
        EmployeesDepsStore.deps = employeesModuleDeps
        EmployeesDepsStore.appComponent = viewModel.employeesAppComponent
    }

    private fun provideProfileDeps() {
        val profileModuleDeps = object : ProfileAppComponentDeps {
            override val currentEmployee: Employee?
                get() = deps!!.currentEmployee
            override val firebaseAuth: FirebaseAuth
                get() = deps!!.firestoreAuth
        }
        viewModel.profileAppComponent = DaggerProfileAppComponent.builder()
            .profileAppComponentDeps(profileModuleDeps).build()
        ProfileDepsStore.deps = profileModuleDeps
        ProfileDepsStore.appComponent = viewModel.profileAppComponent
    }

    private fun provideSettingsDeps() {
        val settingsModuleDeps = object : SettingsAppComponentDeps {
            override val settings: Settings
                get() = deps!!.settings
            override val database: Database
                get() = deps!!.database
            override val sharedPreferences: SharedPreferences
                get() = deps!!.sharedPreferences
            override val currentEmployee: Employee?
                get() = deps!!.currentEmployee
            override val ordersService: OrdersService
                get() = deps!!.ordersService
        }
        viewModel.settingsAppComponent =
            DaggerSettingsAppComponent.builder()
                .settingsAppComponentDeps(settingsModuleDeps).build()
        SettingsDepsStore.deps = settingsModuleDeps
        SettingsDepsStore.appComponent = viewModel.settingsAppComponent
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

    override fun onBackPressed() {
        showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        super.onBackPressed()
    }

    override fun onLeaveAccount() {
        hideNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        LogInDepsStore.deps = deps as LogInDeps
        navController.setGraph(R.navigation.navigation_log_in)
    }

    override fun onAuthorisationEvent(employee: Employee?) {
        AdminDepsStore.employeeAuthCallback!!.onAuthorisationEvent(employee)
    }
}