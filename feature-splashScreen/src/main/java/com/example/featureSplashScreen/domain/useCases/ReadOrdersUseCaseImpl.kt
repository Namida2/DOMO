package com.example.featureSplashScreen.domain.useCases

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.Order
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.extensions.logE
import com.example.featureSplashScreen.data.TaskWithOrders
import com.example.featureSplashScreen.domain.repositories.OrdersRemoteRepository
import javax.inject.Inject

class ReadOrdersUseCaseImpl @Inject constructor(
    private val ordersRemoteRepository: OrdersRemoteRepository,
    private val ordersService: OrdersService<OrdersServiceSub>,
) : ReadOrdersUseCase {
    override fun readOrders() {
        ordersRemoteRepository.readOrders(object : TaskWithOrders {
            override fun onSuccess(result: List<Order>) {
                ordersService.addListOfOrders(result)
            }
            override fun onError(message: ErrorMessage?) {
                logE("$this: $message")
            }
        })
    }
}

interface ReadOrdersUseCase {
    fun readOrders()
}