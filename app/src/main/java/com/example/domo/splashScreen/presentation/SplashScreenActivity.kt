package com.example.domo.splashScreen.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.cookMain.domain.di.CookMainDepsStore
import com.example.cookMain.presentation.CookMainActivity
import com.example.core.domain.Employee
import com.example.core.domain.tools.ErrorMessages.checkNetworkConnectionMessage
import com.example.core.domain.tools.constants.EmployeePosts.ADMINISTRATOR
import com.example.core.domain.tools.constants.EmployeePosts.COOK
import com.example.core.domain.tools.constants.EmployeePosts.WAITER
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.core.domain.tools.extensions.isNetworkConnected
import com.example.core.domain.tools.extensions.logD
import com.example.domo.databinding.ActivityMainBinding
import com.example.domo.splashScreen.domain.ViewModelFactory
import com.example.domo.splashScreen.domain.di.SplashScreenDepsStore
import com.example.featureLogIn.R
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureLogIn.domain.interfaces.EmployeeAuthorizationCallback
import com.example.waiterMain.domain.di.WaiterMainDepsStore
import com.example.waiterMain.presentation.WaiterMainActivity
import extentions.employee
import java.io.Serializable

class SplashScreenActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashScreenViewModel> { ViewModelFactory }
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.findNavController()

    }

    private fun checkPermissions() {
        if (isNetworkConnected())
            viewModel.getCurrentEmployee()
        else createMessageDialog(checkNetworkConnectionMessage) {
            this.finish()
        }?.show(supportFragmentManager, "")
        subscribeToViewModelState()
    }

    private fun doMagic(employee: Employee) {
        this.employee = employee
        WaiterMainDepsStore.deps = SplashScreenDepsStore.appComponent
        WaiterMainDepsStore.profileDeps = SplashScreenDepsStore.appComponent
        startActivity(
            Intent(this, this::class.java)
        )
    }

    private fun subscribeToViewModelState() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is SplashScreenStates.CheckingForCurrentEmployee -> {
                    //progressBar?
                }
                is SplashScreenStates.EmployeeDoesNotExit -> {
                    //TODO: Resolve this problem //STOPPED//
                    LogInDepsStore.deps = SplashScreenDepsStore.appComponent
                    navController.setGraph(
                        R.navigation.navigation_log_in,
                        bundleOf("employeeAuthorizationCallback" to object :
                            EmployeeAuthorizationCallback {
                            override fun onEmployeeLoggedIn(employee: Employee) {
                                logD("it it works?")
                                doMagic(employee)
                            }
                        })
                    )
                    setContentView(binding.root)
                }
                is SplashScreenStates.EmployeeExists -> {
                    //Set an employee in the extension field
                    employee = state.employee
                    when (employee.post) {
                        COOK -> {
                            CookMainDepsStore.deps = SplashScreenDepsStore.appComponent
                            startActivity(Intent(this, CookMainActivity::class.java))
                        }
                        WAITER -> {
                            WaiterMainDepsStore.deps = SplashScreenDepsStore.appComponent
                            WaiterMainDepsStore.profileDeps = SplashScreenDepsStore.appComponent
                            startActivity(Intent(this, WaiterMainActivity::class.java))
                        }
                        ADMINISTRATOR ->
                            startActivity(Intent(this, WaiterMainActivity::class.java))
                    }
                }
                is SplashScreenStates.PermissionError -> {
                    createMessageDialog(state.message)
                        ?.show(supportFragmentManager, "")
                }
                else -> {}//DefaultState
            }
        }
    }
}






