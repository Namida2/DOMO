package com.example.featureSplashScreen.domain.repositories

import com.example.featureSplashScreen.data.TaskWithOrders

interface OrdersRemoteRepository {
    fun readOrders(task: TaskWithOrders)
}