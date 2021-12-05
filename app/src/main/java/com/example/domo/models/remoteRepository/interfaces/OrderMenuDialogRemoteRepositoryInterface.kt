package com.example.domo.models.remoteRepository.interfaces

import entities.order.Order
import entities.tools.SimpleTask

interface OrderMenuDialogRemoteRepositoryInterface {
    fun insertCurrentOrder(order: Order, task: SimpleTask)
}