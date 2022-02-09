package com.example.domo.models.interfaces

import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrderServiceSub

interface WaiterActOrderFragSharedViewModel : OrdersService<OrderServiceSub>