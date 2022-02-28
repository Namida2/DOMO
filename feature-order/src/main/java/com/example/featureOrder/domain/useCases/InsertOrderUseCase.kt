package com.example.featureOrder.domain.useCases

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.featureOrder.data.repositories.OrdersRemoteRepository
import javax.inject.Inject

class InsertOrderUseCaseImpl @Inject constructor(
    private val ordersService: OrdersService<OrdersServiceSub>,
    private val remoteRepository: OrdersRemoteRepository,
) : InsertOrderUseCase {
    override fun insertCurrentOrder(task: com.example.core.domain.tools.SimpleTask) {
        remoteRepository.insertCurrentOrder(ordersService.currentOrder!!, object :
            com.example.core.domain.tools.SimpleTask {
            override fun onSuccess(arg: Unit) {
                ordersService.confirmCurrentOrder()
                task.onSuccess(Unit)
            }

            override fun onError(message: com.example.core.domain.tools.ErrorMessage?) {
                task.onError()
            }
        })
    }
}

interface InsertOrderUseCase {
    fun insertCurrentOrder(task: com.example.core.domain.tools.SimpleTask)
}