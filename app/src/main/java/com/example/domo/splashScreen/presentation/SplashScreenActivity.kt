package com.example.domo.splashScreen.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.domo.authorization.presentation.AuthorizationActivity
import com.example.domo.splashScreen.domain.ViewModelFactory
import com.example.waiterCore.domain.tools.ErrorMessages.networkConnectionMessage
import com.example.waiterCore.domain.tools.extensions.createMessageDialog
import com.example.waiterCore.domain.tools.extensions.isNetworkConnected
import com.example.waiterMain.presentation.WaiterMainActivity
import com.example.waiterCore.domain.tools.constants.EmployeePosts.ADMINISTRATOR
import com.example.waiterCore.domain.tools.constants.EmployeePosts.COOK
import com.example.waiterCore.domain.tools.constants.EmployeePosts.WAITER
import extentions.employee

class SplashScreenActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashScreenViewModel> { ViewModelFactory }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToViewModelState()
        if (isNetworkConnected())
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

