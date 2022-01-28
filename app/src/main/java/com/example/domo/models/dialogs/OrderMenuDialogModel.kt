package com.example.domo.models.dialogs

import com.example.domo.models.OrderServiceSub
import com.example.domo.models.interfaces.OrderMenuDialogModelInterface
import com.example.domo.models.remoteRepository.interfaces.OrderMenuDialogRemoteRepositoryInterface
import com.example.waiterCore.domain.tools.ErrorMessage
import entities.interfaces.OrderServiceInterface
import com.example.waiterCore.domain.tools.SimpleTask
import javax.inject.Inject

class OrderMenuDialogModel @Inject constructor(
    private val orderService: OrderServiceInterface<OrderServiceSub>,
    private val remoteRepository: OrderMenuDialogRemoteRepositoryInterface,
) : OrderMenuDialogModelInterface {
    override fun insertCurrentOrder(task: SimpleTask) {
        remoteRepository.insertCurrentOrder(orderService.currentOrder!!, object : SimpleTask {
            override fun onSuccess(arg: Unit) {
                orderService.confirmCurrentOrder()
                task.onSuccess(Unit)
            }
            override fun onError(message: ErrorMessage?) {
                task.onError()
            }
        })
    }
}