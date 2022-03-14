package com.example.administratorMain.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.administratorMain.R
import com.example.administratorMain.databinding.ActivityAdministratorBinding
import com.example.core.domain.Employee
import com.example.core.domain.interfaces.BasePostActivity
import com.example.featureLogIn.databinding.ActivityAuthorizationBinding

class AdministratorMainActivity : AppCompatActivity(), BasePostActivity {
    private lateinit var binding: ActivityAdministratorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdministratorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onLeaveAccount() {
//        hideNavigationUI()
//        LogInDepsStore.deps = WaiterMainDepsStore.profileDeps as LogInDeps
//        navController.setGraph(R.navigation.navigation_log_in)
    }

    override fun onEmployeeLoggedIn(employee: Employee?) {
//        WaiterMainDepsStore.employeeAuthCallback.onEmployeeLoggedIn(employee)
    }
}