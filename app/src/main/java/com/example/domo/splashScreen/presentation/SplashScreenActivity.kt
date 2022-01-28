package com.example.domo.splashScreen.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domo.authorization.presentation.AuthorizationActivity
import com.example.domo.splashScreen.domain.di.DaggerSplashScreenAppComponent
import com.example.domo.splashScreen.domain.di.SplashScreenDepsStore
import com.example.domo.views.activities.WaiterMainActivity
import com.example.waiterCore.domain.tools.ErrorMessages.networkConnectionMessage
import entities.constants.EmployeePosts.ADMINISTRATOR
import entities.constants.EmployeePosts.COOK
import entities.constants.EmployeePosts.WAITER
import extentions.createMessageDialog
import extentions.employee
import extentions.isNetworkConnected

class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: SplashScreenViewModel by viewModels {
        object : ViewModelProvider.Factory {
            val appComponent = DaggerSplashScreenAppComponent.builder().putDeps(
                SplashScreenDepsStore.deps!!
            ).build()
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                SplashScreenViewModel(
                    appComponent.provideReadMenuUseCase(),
                    appComponent.provideGetCurrentEmployeeUseCase()
                ) as T
        }
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToViewModelState()
        if(isNetworkConnected())
            viewModel.getCurrentEmployee()
        else createMessageDialog(networkConnectionMessage) {
            this.finish()
        }?.show(supportFragmentManager, "")
    }

    private fun subscribeToViewModelState() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is SplashScreenStates.CheckingForCurrentEmployee -> {
                    //progressBar?
                }
                is SplashScreenStates.EmployeeDoesNotExit ->
                    startActivity(Intent(this, AuthorizationActivity::class.java))
                is SplashScreenStates.EmployeeExists -> {
                    //Set an employee in the extension field
                    employee = state.employee
                    when (employee?.post) {
                        COOK ->
                            startActivity(Intent(this, WaiterMainActivity::class.java))
                        WAITER ->
                            startActivity(Intent(this, WaiterMainActivity::class.java))
                        ADMINISTRATOR ->
                            startActivity(Intent(this, WaiterMainActivity::class.java))
                    }
                }
                is SplashScreenStates.PermissionError -> {
                    createMessageDialog(state.message)
                        ?.show(supportFragmentManager, "")
                }
                else -> {
                }//DefaultState
            }
        }
    }
}
