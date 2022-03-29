package com.example.featureCurrentOrders.domain.repositories

import com.example.core.domain.entities.tools.SimpleTask

interface OrdersRemoteRepository {
    fun deleteOrder(orderId: Int, task: SimpleTask)
}