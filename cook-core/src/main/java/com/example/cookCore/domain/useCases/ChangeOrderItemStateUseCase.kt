package com.example.cookCore.domain.useCases

import com.example.cookCore.data.repositories.OrderItemsRemoteRepository
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.SimpleTask
import javax.inject.Inject

class ChangeOrderItemStateUseCaseImpl @Inject constructor(
    private val orderItemsRepository: OrderItemsRemoteRepository
): ChangeOrderItemStateUseCase {

    override fun setOrderItemAsReady(orderId: Int, orderItemId: String, task: SimpleTask) {
        orderItemsRepository.setOrderItemAsReady(orderId, orderItemId, object: SimpleTask {
            override fun onSuccess(arg: Unit) {
                TODO("Not yet implemented")
            }
            override fun onError(message: ErrorMessage?) {
                TODO("Not yet implemented")
            }
        })

    }
}

interface ChangeOrderItemStateUseCase {
    fun setOrderItemAsReady(orderId: Int, orderItemId: String, task: SimpleTask)
}
