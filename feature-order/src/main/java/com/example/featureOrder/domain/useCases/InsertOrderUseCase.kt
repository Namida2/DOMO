package com.example.featureOrder.domain.useCases

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.entities.order.OrdersServiceSub
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import com.example.featureOrder.data.repositories.OrdersRemoteRepository
import javax.inject.Inject

class InsertOrderUseCaseImpl @Inject constructor(
    private val ordersService: OrdersService,
    private val remoteRepository: OrdersRemoteRepository,
) : InsertOrderUseCase {
    override fun insertCurrentOrder(task: SimpleTask) {
        remoteRepository.insertCurrentOrder(ordersService.currentOrder!!, object: SimpleTask {
            override fun onSuccess(result: Unit) {
                ordersService.confirmCurrentOrder()
                task.onSuccess(Unit)
            }
            override fun onError(message: ErrorMessage?) {
                task.onError(message)
            }
        })
    }
}

interface InsertOrderUseCase {
    fun insertCurrentOrder(task: SimpleTask)
}