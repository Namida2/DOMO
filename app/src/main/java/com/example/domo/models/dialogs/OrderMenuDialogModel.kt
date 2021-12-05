package com.example.domo.models.dialogs

import com.example.domo.models.OrderServiceSub
import com.example.domo.models.interfaces.OrderMenuDialogModelInterface
import com.example.domo.models.remoteRepository.interfaces.OrderMenuDialogRemoteRepositoryInterface
import database.daos.OrderDao
import entities.ErrorMessage
import entities.interfaces.OrderServiceInterface
import entities.tools.SimpleTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderMenuDialogModel @Inject constructor(
    private val orderService: OrderServiceInterface<OrderServiceSub>,
    private val remoteRepository: OrderMenuDialogRemoteRepositoryInterface,
    private val orderDao: OrderDao,
) : OrderMenuDialogModelInterface {
    override fun insertCurrentOrder(task: SimpleTask) {
        remoteRepository.insertCurrentOrder(orderService.currentOrder!!, object : SimpleTask {
            override fun onSuccess(arg: Unit) {
                CoroutineScope(IO).launch {
                    orderDao.insert(orderService.currentOrder!!)
                    withContext(Main) {
                        task.onSuccess(Unit)
                    }
                    orderService.confirmCurrentOrder()
                }
            }
            override fun onError(message: ErrorMessage?) {
                task.onError()
            }
        })
    }
}