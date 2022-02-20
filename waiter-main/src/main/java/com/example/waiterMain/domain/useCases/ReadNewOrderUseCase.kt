package com.example.waiterMain.domain.useCases


import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.Order
import com.example.waiterCore.domain.order.OrdersServiceSub
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.TaskWithOrder
import com.example.waiterMain.data.OrdersRemoteRepository
import javax.inject.Inject

class ReadNewOrderUseCaseImpl @Inject constructor(
    private val ordersService: OrdersService<OrdersServiceSub>,
    private val ordersRepo: OrdersRemoteRepository
): ReadNewOrderUseCase {

    override fun readNewOrder(order: Order, onError: (message: ErrorMessage) -> Unit) {
        ordersRepo.readOrderItems(order, object : TaskWithOrder {
            override fun onSuccess(arg: Order) {
                ordersService.addOrder(arg)
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