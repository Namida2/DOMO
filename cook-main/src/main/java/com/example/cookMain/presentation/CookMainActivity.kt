package com.example.cookMain.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookMain.databinding.ActivityCookMainBinding
import com.example.cookMain.domain.di.CookMainDepsStore
import com.example.core.domain.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponentDeps

class CookMainActivity : AppCompatActivity() {

    private lateinit var bidning: ActivityCookMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bidning = ActivityCookMainBinding.inflate(layoutInflater)
        setContentView(bidning.root)
        provideCurrentOrderDeps()
    }

    private fun provideCurrentOrderDeps() {
        CurrentOrderDepsStore.deps = object : CurrentOrdersAppComponentDeps {
            override val currentEmployee: Employee?
                get() = CookMainDepsStore.deps.currentEmployee
            override val ordersService: OrdersService<OrdersServiceSub>
                get() = CookMainDepsStore.deps.ordersService
        }
    }

    private fun mareWorkerRequests() {

    }
}