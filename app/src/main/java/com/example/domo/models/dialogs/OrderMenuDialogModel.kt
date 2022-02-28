package com.example.domo.models.dialogs

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.domo.models.interfaces.OrderMenuDialogModelInterface
import com.example.domo.models.remoteRepository.interfaces.OrderMenuDialogRemoteRepositoryInterface
import javax.inject.Inject

class OrderMenuDialogModel @Inject constructor(
    private val ordersService: OrdersService<OrdersServiceSub>,
    private val remoteRepository: OrderMenuDialogRemoteRepositoryInterface,
) : OrderMenuDialogModelInterface {
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