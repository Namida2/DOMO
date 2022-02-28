package com.example.domo.models.remoteRepository.interfaces

import com.example.core.domain.order.Order

interface OrderMenuDialogRemoteRepositoryInterface {
    fun insertCurrentOrder(order: Order, task: com.example.core.domain.tools.SimpleTask)
}