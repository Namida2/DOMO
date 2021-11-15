package com.example.domo.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.domo.viewModels.SplashScreenStates
import com.example.domo.viewModels.SplashScreenViewModel
import com.example.domo.viewModels.ViewModelFactory
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import constants.EmployeePosts.ADMINISTRATOR
import constants.EmployeePosts.COOK
import constants.EmployeePosts.WAITER
import constants.FirestoreConstants.COLLECTION_DISHES
import constants.FirestoreConstants.COLLECTION_MENU
import constants.FirestoreConstants.COLLECTION_RESTAURANTS
import constants.FirestoreConstants.DOCUMENT_DOMO
import database.Database
import database.daos.EmployeeDao
import entities.Dish
import extentions.appComponent
import extentions.employee
import javax.inject.Inject


class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: SplashScreenViewModel by viewModels { ViewModelFactory(appComponent) }
    val db = FirebaseFirestore.getInstance()

    private val menuCollectionRef: CollectionReference =
        db.collection(COLLECTION_RESTAURANTS).document(DOCUMENT_DOMO).collection(COLLECTION_MENU)

    @Inject
    lateinit var database: Database

    @Inject
    lateinit var employeeDao: EmployeeDao

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        viewModel.getCurrentEmployee()
        subscribeToViewModelState()

//        log(FirebaseAuth.getInstance().currentUser.toString())
//
//        val db = FirebaseFirestore.getInstance()
//
//        db.runTransaction {
//            menuCollectionRef.get().addOnSuccessListener {
//                it.forEach {
//
//                }
//            }.addOnFailureListener {
//                log(it.toString())
//            }
//        }

    }

    private fun readAllFromCategory(transaction: Transaction, categoryId: String) {
        val dishesCollectionRef = menuCollectionRef.document(categoryId).collection(COLLECTION_DISHES)
        menuCollectionRef.document(categoryId).collection(COLLECTION_DISHES).get().addOnSuccessListener {
            it.forEach { dish ->
                transaction.get(dishesCollectionRef.document(dish.id)).toObject(Dish::class.java)
            }
        }.addOnFailureListener {
            log(it.toString())
        }
    }


    private fun readMenuCategories(): Task<QuerySnapshot> =
        db.collection(COLLECTION_RESTAURANTS).get()

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

fun Any.log(message: String) {
    Log.d("MyLogging", message)
}