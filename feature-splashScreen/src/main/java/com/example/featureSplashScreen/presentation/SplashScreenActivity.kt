package com.example.featureSplashScreen.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.administratorMain.domatn.di.AdminDepsStore
import com.example.administratorMain.presentation.AdministratorMainActivity
import com.example.cookMain.domain.di.CookMainDepsStore
import com.example.cookMain.presentation.CookMainActivity
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.constants.EmployeePosts.*
import com.example.core.domain.entities.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.entities.tools.extensions.isNetworkConnected
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.core.domain.interfaces.NewMenuVersionCallback
import com.example.core.domain.interfaces.OnNetworkConnectionLostCallback
import com.example.featureLogIn.R
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureSplashScreen.databinding.ActivitySplashScreenBinding
import com.example.featureSplashScreen.domain.ViewModelFactory
import com.example.featureSplashScreen.domain.di.SplashScreenDepsStore
import com.example.featureSplashScreen.domain.di.SplashScreenDepsStore.setNewEmployeeData
import com.example.waiterMain.domain.di.WaiterMainDepsStore
import com.example.waiterMain.presentation.WaiterMainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity(), EmployeeAuthCallback, NewMenuVersionCallback,
    OnNetworkConnectionLostCallback {

    private val viewModel by viewModels<SplashScreenViewModel> { ViewModelFactory }
    private lateinit var navController: NavController
    private lateinit var binding: ActivitySplashScreenBinding

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkInternetConnection()
    }

    private fun checkInternetConnection() {
        if (isNetworkConnected()) {
            viewModel.getCurrentEmployee()
            viewModel.readSettings()
        } else createMessageDialog(checkNetworkConnectionMessage) {
            this.finish()
        }?.show(supportFragmentManager, "")
        subscribeToViewModelState()
    }

    private fun subscribeToViewModelState() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is SplashScreenStates.ReadingData -> {
                    //progressBar?
                }
                is SplashScreenStates.EmployeeDoesNotExists -> {
                    prepareLogInScreen()
                }
                is SplashScreenStates.EmployeeAndSettingsExist -> {
                    setNewEmployeeData(state.employee)
                    when (state.employee.post) {
                        COOK.value -> {
                            CookMainDepsStore.deps = SplashScreenDepsStore.appComponent
                            CookMainDepsStore.employeeAuthCallback = this
                            CookMainDepsStore.newMenuVersionCallback = this
                            CookMainDepsStore.onNetworkConnectionLostCallback = this
                            startActivity(Intent(this, CookMainActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            })
                        }
                        WAITER.value -> {
                            WaiterMainDepsStore.deps = SplashScreenDepsStore.appComponent
                            WaiterMainDepsStore.profileDeps = SplashScreenDepsStore.appComponent
                            WaiterMainDepsStore.employeeAuthCallback = this
                            WaiterMainDepsStore.newMenuVersionCallback = this
                            WaiterMainDepsStore.onNetworkConnectionLostCallback = this
                            startActivity(Intent(this, WaiterMainActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            })
                        }
                        ADMINISTRATOR.value -> {
                            AdminDepsStore.deps = SplashScreenDepsStore.appComponent
                            AdminDepsStore.employeeAuthCallback = this
                            AdminDepsStore.onNetworkConnectionLostCallback = this
                            startActivity(Intent(this, AdministratorMainActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            })
                        }
                    }
                }
                is SplashScreenStates.OnFailure -> {
                    createMessageDialog(state.message) {
                        finish()
                    }?.show(supportFragmentManager, "")
                }
                SplashScreenStates.DefaultState -> {}
            }
        }
    }

    private fun prepareLogInScreen() {
        if (this::navController.isInitialized) return
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.findNavController().also {
            it.setGraph(R.navigation.navigation_log_in)
        }
        LogInDepsStore.deps = SplashScreenDepsStore.appComponent
        setContentView(binding.root)
    }

    override fun onAuthorisationEvent(employee: Employee?) {
        employee?.let { setNewEmployeeData(it) }
        startActivity(Intent(this, SplashScreenActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    override fun onNewMenu() {
        startActivity(Intent(this, SplashScreenActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    override fun onConnectionLost() {
        startActivity(Intent(this, SplashScreenActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}






