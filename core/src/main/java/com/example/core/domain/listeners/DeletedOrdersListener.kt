package com.example.core.domain.listeners

import kotlinx.coroutines.flow.Flow

interface DeletedOrdersListener {
    val deletedOrdersInfo: Flow<Int>
}