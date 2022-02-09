package com.example.domo.models.dialogs

import com.example.domo.models.interfaces.OrderMenuDialogModelInterface
import com.example.domo.models.remoteRepository.interfaces.OrderMenuDialogRemoteRepositoryInterface
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrderServiceSub
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.SimpleTask
import javax.inject.Inject

class OrderMenuDialogModel @Inject constructor(
    private val ordersService: OrdersService<OrderServiceSub>,
    private val remoteRepository: OrderMenuDialogRemoteRepositoryInterface,
) : OrderMenuDialogModelInterface {
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