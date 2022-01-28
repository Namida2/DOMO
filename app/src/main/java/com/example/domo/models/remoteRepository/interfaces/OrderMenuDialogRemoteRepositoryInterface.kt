package com.example.domo.models.remoteRepository.interfaces

import com.example.waiterCore.domain.order.Order
import com.example.waiterCore.domain.tools.SimpleTask

interface OrderMenuDialogRemoteRepositoryInterface {
    fun insertCurrentOrder(order: Order, task: SimpleTask)
}