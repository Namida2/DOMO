package com.example.feature_splashscreen.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashScreenViewModel

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel.getCurrentEmployee()
        subscribeToViewModelState()
    }

    private fun subscribeToViewModelState() {
//        viewModel.state.observe(this) { state ->
//            when (state) {
//                is SplashScreenStates.CheckingForCurrentEmployee -> {
//                    //progressBar?
//                }
//                is SplashScreenStates.EmployeeDoesNotExit ->
//                    startActivity(Intent(this, AuthorizationActivity::class.java))
//                is SplashScreenStates.EmployeeExists -> {
//                    employee = state.employee
//                    when (employee?.post) {
//                        COOK ->
//                            startActivity(Intent(this, WaiterMainActivity::class.java))
//                        WAITER ->
//                            startActivity(Intent(this, WaiterMainActivity::class.java))
//                        ADMINISTRATOR ->
//                            startActivity(Intent(this, WaiterMainActivity::class.java))
//                    }
//                }
//                else -> {
//                }//DefaultState
//            }
//        }
    }
}
