package com.example.administratorMain.domatn.di

import com.example.core.domain.entities.Settings
import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.OrdersService
import com.google.firebase.auth.FirebaseAuth
import dagger.Component

@Component(dependencies = [AdminAppComponentDeps::class])
interface AdminAppComponent

interface AdminAppComponentDeps {
    val settings: Settings
    val currentEmployee: Employee?
    val firestoreAuth: FirebaseAuth
    val ordersService: OrdersService
}