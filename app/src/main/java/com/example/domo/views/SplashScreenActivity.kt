package com.example.domo.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import application.appComponent
import application.employee
import com.example.domo.databinding.ActivitySplashScreenBinding
import com.example.domo.viewModels.SplashScreenStates
import com.example.domo.viewModels.SplashScreenViewModel
import com.example.domo.viewModels.ViewModelFactory
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import constants.EmployeePosts.ADMINISTRATOR
import constants.EmployeePosts.COOK
import constants.EmployeePosts.WAITER
import constants.FirestoreConstants.COLLECTION_RESTAURANTS
import database.Database
import database.daos.EmployeeDao
import javax.inject.Inject


class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: SplashScreenViewModel by viewModels { ViewModelFactory(appComponent) }

    val db = FirebaseFirestore.getInstance()

    @Inject
    lateinit var database: Database

    @Inject
    lateinit var employeeDao: EmployeeDao

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        viewModel.getCurrentEmployee()
        //subscribeToViewModelState()
        //TODO: Add bottomSheetDialogMenu


        val db = FirebaseFirestore.getInstance()

        db.runTransaction {
            db.collection(COLLECTION_RESTAURANTS).get().addOnCompleteListener {

            }
        }
    }

    private fun readMenuCategories(): Task<QuerySnapshot>
    = db.collection(COLLECTION_RESTAURANTS).get()

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
                else -> {}//DefaultState
            }
        }
    }
}

fun Any.log(message: String) {
    Log.d("MyLogging", message)
}