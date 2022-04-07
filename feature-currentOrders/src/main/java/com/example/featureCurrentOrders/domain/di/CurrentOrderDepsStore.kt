package com.example.featureCurrentOrders.domain.di

import com.example.featureCurrentOrders.domain.interfaces.OnShowOrderDetailCallback

object CurrentOrderDepsStore {
    lateinit var deps: CurrentOrdersAppComponentDeps
    lateinit var appComponent: CurrentOrdersAppComponent
    lateinit var onShowOrderDetailCallback: OnShowOrderDetailCallback
}