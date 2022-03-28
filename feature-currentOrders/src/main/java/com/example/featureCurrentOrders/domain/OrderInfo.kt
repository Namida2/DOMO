package com.example.featureCurrentOrders.domain

import com.example.core.domain.order.Order

data class OrderInfo(val order: Order, val isCompleted: Boolean)