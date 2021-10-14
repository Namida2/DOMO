package com.example.domo.views

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import application.appComponent
import application.employee
import com.example.domo.databinding.ActivitySplashScreenBinding
import com.example.domo.viewModels.SplashScreenStates
import com.example.domo.viewModels.SplashScreenViewModel
import com.example.domo.viewModels.ViewModelFactory
import constants.EmployeePosts.ADMINISTRATOR
import constants.EmployeePosts.COOK
import constants.EmployeePosts.WAITER
import database.Database
import database.EmployeeDao
import javax.inject.Inject


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: SplashScreenViewModel by viewModels { ViewModelFactory(appComponent) }
    @Inject
    lateinit var database: Database
    @Inject
    lateinit var employeeDao: EmployeeDao

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        appComponent.inject(this)
        viewModel.state.observe(this) { state ->
            when(state) {
                is SplashScreenStates.CheckingForCurrentEmployee -> {
                    //progressBar?
                }
                is SplashScreenStates.EmployeeDoesNotExit -> {
                    //startActivity(Intent(this, LogInActivity::class.java))
                }
                is SplashScreenStates.EmployeeExists -> {
                    employee = state.employee
                    when(employee?.post) {
                        COOK -> {
                            //CookMainActivity
                        }
                        WAITER -> {
                            startActivity(Intent(this, WaiterMainActivity::class.java))
                        }
                        ADMINISTRATOR -> {
                            //AdministratorMainActivity
                        }
                    }

                }
                else -> {} //DefaultState
            }
        }
        startActivity(Intent(this, WaiterMainActivity::class.java))
    }

}
fun Any.log(message: String) {
    Log.d("MyLogging", message)
}