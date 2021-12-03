package com.example.domo.models.dialogs

import com.example.domo.models.remoteRepository.OrderMenuDialogRemoteRepository
import database.daos.OrderItemDao
import javax.inject.Inject

class OrderMenuDialogModel @Inject constructor(
    private val remoteRepository: OrderMenuDialogRemoteRepository,
    private val orderItemDao: OrderItemDao
) {
}