package com.example.domo.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.domo.viewModels.ViewModelFactory
import com.example.domo.viewModels.activities.SplashScreenStates
import com.example.domo.viewModels.activities.SplashScreenViewModel
import com.example.core.domain.tools.constants.EmployeePosts.ADMINISTRATOR
import com.example.core.domain.tools.constants.EmployeePosts.COOK
import com.example.core.domain.tools.constants.EmployeePosts.WAITER
import extentions.employee


class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: SplashScreenViewModel by viewModels { ViewModelFactory }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //startActivity(Intent(this, MainActivity::class.java))
        viewModel.getCurrentEmployee()
        subscribeToViewModelState()
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
                else -> {
                }//DefaultState
            }
        }
    }
}
