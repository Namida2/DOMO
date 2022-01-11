package com.example.domo.models.remoteRepository.interfaces

import com.example.waiter_core.domain.order.Order
import com.example.waiter_core.domain.tools.SimpleTask

interface OrderMenuDialogRemoteRepositoryInterface {
    fun insertCurrentOrder(order: Order, task: SimpleTask)
}