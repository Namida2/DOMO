package com.example.cookCore.domain.useCases

import com.example.cookCore.data.repositories.OrderItemsRemoteRepository
import com.example.core.domain.entities.order.OrderItem
import com.example.core.domain.entities.tools.SimpleTask
import javax.inject.Inject

class ChangeOrderItemStateUseCaseImpl @Inject constructor(
    private val orderItemsRepository: OrderItemsRemoteRepository
) : ChangeOrderItemStateUseCase {

    override fun changeOrderItemStatus(orderId: Int, orderItem: OrderItem, task: SimpleTask) {
        orderItemsRepository.changeOrderItemStatus(orderId, orderItem.getOrderIemId(), !orderItem.isReady, task)
    }
}

interface ChangeOrderItemStateUseCase {
    fun changeOrderItemStatus(orderId: Int, orderItem: OrderItem, task: SimpleTask)
}
