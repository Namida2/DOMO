package com.example.featureCurrentOrders.domain.useCases

import com.example.core.domain.entities.tools.SimpleTask
import com.example.featureCurrentOrders.domain.repositories.OrdersRemoteRepository
import javax.inject.Inject

class DeleteOrderUseCase @Inject constructor(
    private val ordersRemoteRepository: OrdersRemoteRepository
) {
    fun deleteOrder(orderId: Int, task: SimpleTask) {
        ordersRemoteRepository.deleteOrder(orderId, task)
    }
}