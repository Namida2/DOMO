package com.example.featureOrder.domain.useCases

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.SimpleTask
import com.example.featureOrder.data.repositories.OrdersRemoteRepository
import javax.inject.Inject

class InsertOrderUseCaseImpl @Inject constructor(
    private val ordersService: OrdersService<OrdersServiceSub>,
    private val remoteRepository: OrdersRemoteRepository,
) : InsertOrderUseCase {
    override fun insertCurrentOrder(task: SimpleTask) {
        remoteRepository.insertCurrentOrder(ordersService.currentOrder!!, object: SimpleTask {
            override fun onSuccess(arg: Unit) {
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