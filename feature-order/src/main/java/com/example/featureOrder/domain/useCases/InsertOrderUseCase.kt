package com.example.featureOrder.domain.useCases

import com.example.featureOrder.data.repositories.OrdersRemoteRepository
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrdersServiceSub
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.SimpleTask
import javax.inject.Inject

class InsertOrderUseCaseImpl @Inject constructor(
    private val ordersService: OrdersService<OrdersServiceSub>,
    private val remoteRepository: OrdersRemoteRepository,
): InsertOrderUseCase {
    override fun insertCurrentOrder(task: SimpleTask) {
        remoteRepository.insertCurrentOrder(ordersService.currentOrder!!, object : SimpleTask {
            override fun onSuccess(arg: Unit) {
                ordersService.confirmCurrentOrder()
                task.onSuccess(Unit)
            }
            override fun onError(message: ErrorMessage?) {
                task.onError()
            }
        })
    }
}
interface InsertOrderUseCase {
    fun insertCurrentOrder(task: SimpleTask)
}