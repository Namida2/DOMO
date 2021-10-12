package com.example.domo.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import application.MyApplication
import application.appComponent
import application.currentEmployee
import com.example.domo.databinding.ActivitySplashScreenBinding
import com.example.domo.viewModels.SplashScreenStates
import com.example.domo.viewModels.SplashScreenViewModel
import com.example.domo.viewModels.ViewModelFactory
import database.Database
import database.EmployeeDao
import entities.Employee
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
        appComponent.inject(this)
        currentEmployee = Employee(0, "name1", "sahfkashfkaf")
        log(currentEmployee.toString())
        viewModel.state.observe(this) {
            when(it) {
                is SplashScreenStates.CheckingForCurrentEmployee -> {

                }
                is SplashScreenStates.EmployeeExists -> {

                }
            }

        }

    }

}
fun Any.log(message: String) {
    Log.d("MyLogging", message)
}