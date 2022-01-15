package com.example.feature_splashscreen.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.feature_splashscreen.domain.di.DaggerSplashScreenAppComponent
import com.example.feature_splashscreen.domain.di.SplashScreenDepsStore

class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: SplashScreenViewModel by viewModels {
        object : ViewModelProvider.Factory {

            val appComponent = DaggerSplashScreenAppComponent.builder().putDeps(
                SplashScreenDepsStore.deps!!
            ).build()

            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                SplashScreenViewModel(
                    appComponent.provideReadMenuUseCase()
                    appComponent.provi()
                ) as T

        }
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCurrentEmployee()
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
