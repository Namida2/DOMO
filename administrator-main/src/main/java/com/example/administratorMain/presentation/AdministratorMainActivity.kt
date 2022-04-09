package com.example.administratorMain.presentation

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
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
import com.example.core.data.workers.NewOrderItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.Settings
import com.example.core.domain.entities.tools.constants.Messages
import com.example.core.domain.entities.tools.constants.Messages.permissionDeniedMessage
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.interfaces.BasePostActivity
import com.example.core.domain.interfaces.OrdersService
import com.example.featureEmployees.domain.di.DaggerEmployeesAppComponent
import com.example.featureEmployees.domain.di.EmployeesAppComponentDeps
import com.example.featureEmployees.domain.di.EmployeesDepsStore
import com.example.featureLogIn.domain.di.LogInDeps
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureProfile.domain.di.DaggerProfileAppComponent
import com.example.featureProfile.domain.di.ProfileAppComponentDeps
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.example.featureRegistration.presentation.RegistrationFragment
import com.example.featureSettings.domain.di.DaggerSettingsAppComponent
import com.example.featureSettings.domain.di.SettingsAppComponentDeps
import com.example.featureSettings.domain.di.SettingsDepsStore
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class AdministratorMainActivity : BasePostActivity(), NavController.OnDestinationChangedListener {

    private val currentDestination = 0
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
        initBinding()
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController.apply {
            addOnDestinationChangedListener(this@AdministratorMainActivity)
        }
        showNavigationUI(binding.root, binding.appBar, binding.bottomNavigation)
        makeWorkerRequests()
        setOnNavigationItemSelectedListener()
        observeOnNewPermissionEvent()
        setContentView(binding.root)
    }

    private fun initBinding() {
        binding = ActivityAdministratorBinding.inflate(layoutInflater).also {
            it.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
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

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (destination.id) {
            R.id.employeesFragment -> {
                binding.title.text = resources.getString(R.string.employees)
            }
            R.id.settingsFragment -> {
                binding.title.text = resources.getString(R.string.settings)
            }
            R.id.profileFragment -> {
                binding.title.text = resources.getString(R.string.profile)
            }
        }
        binding.bottomNavigation.selectedItemId = destination.id
    }

    override fun setOnNavigationItemSelectedListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            if(navController.currentDestination?.id == it.itemId)
                return@setOnItemSelectedListener true
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
                else -> { false }
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
            PeriodicWorkRequestBuilder<NewOrderItemStatusWorker>(
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MINUTES
            ).build()
        WorkManager.getInstance(this).enqueue(newOrdersWorkRequest)
        WorkManager.getInstance(this).enqueue(newOrderItemsStateRequest)
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
        AdminDepsStore.employeeAuthCallback?.onAuthorisationEvent(employee)
        viewModelStore.clear()
    }
}