package com.example.core.domain.useCases


import com.example.core.data.repositorues.OrdersRemoteRepository
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.entities.order.Order
import com.example.core.domain.entities.order.OrdersServiceSub
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.TaskWithOrder
import javax.inject.Inject

class ReadNewOrderUseCaseImpl @Inject constructor(
    private val ordersService: OrdersService,
    private val ordersRepo: OrdersRemoteRepository
) : ReadNewOrderUseCase {

    override fun readNewOrder(order: Order, onError: (message: ErrorMessage) -> Unit) {
        ordersRepo.readOrderItems(order, object : TaskWithOrder {
            override fun onSuccess(result: Order) {
                ordersService.addOrder(result)
            }

            override fun onError(message: ErrorMessage?) {
                TODO("Not yet implemented")
            }
        })
    }
}

interface ReadNewOrderUseCase {
    fun readNewOrder(order: Order, onError: (message: ErrorMessage) -> Unit)
}