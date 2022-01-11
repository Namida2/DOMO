package com.example.domo.models.interfaces

import com.example.waiter_core.domain.tools.SimpleTask

interface OrderMenuDialogModelInterface {
    fun insertCurrentOrder(task: SimpleTask)
}