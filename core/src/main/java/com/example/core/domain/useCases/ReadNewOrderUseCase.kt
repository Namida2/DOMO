package com.example.core.domain.useCases

import com.example.core.data.repositorues.OrdersRemoteRepository
import com.example.core.domain.entities.order.Order
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.TaskWithOrder
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.interfaces.OrdersService
import javax.inject.Inject

class ReadNewOrderUseCaseImpl @Inject constructor(
    private val ordersService: OrdersService,
    private val ordersRepo: OrdersRemoteRepository
) : ReadNewOrderUseCase {

    override fun readNewOrder(order: Order, myOnError: (message: ErrorMessage) -> Unit) {
        ordersRepo.readOrderItems(order, object : TaskWithOrder {
            override fun onSuccess(result: Order) {
                ordersService.addOrder(result)
            }

            override fun onError(message: ErrorMessage?) {
                myOnError(message ?: defaultErrorMessage)
            }
        })
    }
}

interface ReadNewOrderUseCase {
    fun readNewOrder(order: Order, myOnError: (message: ErrorMessage) -> Unit = {})
}