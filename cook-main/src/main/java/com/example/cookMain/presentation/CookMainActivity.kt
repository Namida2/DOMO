package com.example.cookMain.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.cookMain.databinding.ActivityCookMainBinding
import com.example.cookMain.domain.di.CookMainDepsStore
import com.example.core.data.workers.NewOrdersItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponentDeps
import java.util.concurrent.TimeUnit

class CookMainActivity : AppCompatActivity() {

    private lateinit var bidning: ActivityCookMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bidning = ActivityCookMainBinding.inflate(layoutInflater)
        setContentView(bidning.root)
        provideCurrentOrderDeps()
        makeWorkerRequests()
    }

    private fun provideCurrentOrderDeps() {
        CurrentOrderDepsStore.deps = object : CurrentOrdersAppComponentDeps {
            override val currentEmployee: Employee?
                get() = CookMainDepsStore.deps.currentEmployee
            override val ordersService: OrdersService<OrdersServiceSub>
                get() = CookMainDepsStore.deps.ordersService
        }
    }

    private fun makeWorkerRequests() {
        val newOrdersWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersWorker>(MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MINUTES).build()
        val newOrderItemsStateRequest: WorkRequest =
            PeriodicWorkRequestBuilder<NewOrdersItemStatusWorker>(MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(newOrdersWorkRequest)
        WorkManager.getInstance(this).enqueue(newOrderItemsStateRequest)
        observeOrdersWorkerEvents()
    }

    private fun observeOrdersWorkerEvents() {
        NewOrdersWorker.event.observe(this) {
            val data = it.getData() ?: return@observe
            createMessageDialog(data)?.show(supportFragmentManager, "")
        }
    }
}