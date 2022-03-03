package com.example.cookCore.domain.useCases

import com.example.cookCore.data.repositories.OrderItemsRemoteRepository
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.SimpleTask
import javax.inject.Inject

class ChangeOrderItemStateUseCaseImpl @Inject constructor(
    private val orderItemsRepository: OrderItemsRemoteRepository
): ChangeOrderItemStateUseCase {

    override fun setOrderItemAsReady(orderId: Int, orderItemId: String, task: SimpleTask) {
        orderItemsRepository.setOrderItemAsReady(orderId, orderItemId, task)
    }
}

interface ChangeOrderItemStateUseCase {
    fun setOrderItemAsReady(orderId: Int, orderItemId: String, task: SimpleTask)
}
