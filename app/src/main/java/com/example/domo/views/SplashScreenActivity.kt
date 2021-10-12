package com.example.domo.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import application.appComponent
import com.example.domo.databinding.ActivitySplashScreenBinding
import com.example.domo.viewModels.SplashScreenViewModel
import com.example.domo.viewModels.ViewModelFactory
import database.Database
import database.EmployeeDao
import entities.Employee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        CoroutineScope(IO).launch {
            fun1()
            launch {
                fun2()
            }
        }
    }
    suspend fun fun1() {
        repeat(5) {
            log("+++++")
            delay(100)
        }
    }
    suspend fun fun2() {
        repeat(5) {
            log("-----")
            delay(200)
        }
    }
}
fun Any.log(message: String) {
    Log.d("MyLogging", message)
}