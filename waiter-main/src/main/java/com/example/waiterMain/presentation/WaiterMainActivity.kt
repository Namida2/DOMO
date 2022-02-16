package com.example.waiterMain.presentation

import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.CurrentOrdersAppComponentDeps
import com.example.featureOrder.domain.di.OrderAppComponentDeps
import com.example.featureOrder.domain.di.OrderDepsStore
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.CurrentOrderServiceSub
import com.example.waiterCore.domain.order.OrdersServiceSub
import com.example.waiterCore.domain.tools.extensions.Animations.prepareHide
import com.example.waiterCore.domain.tools.extensions.Animations.prepareShow
import com.example.waiterCore.domain.tools.extensions.Animations.prepareSlideDown
import com.example.waiterCore.domain.tools.extensions.Animations.prepareSlideUp
import com.example.waiterMain.R
import com.example.waiterMain.databinding.ActivityWaiterMainBinding
import com.example.waiterMain.domain.di.WaiterMainDepsStore

//TODO: Add a service for listening to new orders
class WaiterMainActivity: AppCompatActivity(),
    NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityWaiterMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaiterMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController.apply {
            addOnDestinationChangedListener(this@WaiterMainActivity)
        }
        setOnItemSelectedListener()
        provideFeatureOrderDeps()
        provideCurrentOrderDeps()
    }

    private fun provideFeatureOrderDeps() {
        OrderDepsStore.deps = object: OrderAppComponentDeps {
            override val ordersService: OrdersService<OrdersServiceSub>
                get() = WaiterMainDepsStore.deps.ordersService

        }
    }

    private fun provideCurrentOrderDeps() {
        CurrentOrderDepsStore.deps = object: CurrentOrdersAppComponentDeps {
            override val ordersService: OrdersService<OrdersServiceSub>
                get() = WaiterMainDepsStore.deps.ordersService
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (destination.id) {
            R.id.orderFragment -> {
                hideNavigationUI()
            }
            R.id.tablesFragment -> {
                showNavigationUI()
            }
        }
    }

    private fun setOnItemSelectedListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.tablesFragment -> {
                    navController.navigate(R.id.navigation_order)
                    true
                }
                R.id.currentOrdersFragment -> {

                    navController.navigate(R.id.currentOrdersFragment)
                    true
                }
                else -> { false}
            }
        }
    }

    private fun hideNavigationUI() {
        with(binding) {
            appBar.prepareHide(appBar.height).start()
            val scrollBounds = Rect()
            rootCoordinationLayout.getHitRect(scrollBounds)
            if (bottomNavigation.getLocalVisibleRect(scrollBounds)) {
                bottomNavigation.prepareSlideDown(bottomNavigation.height).start()
            }
        }
    }

    private fun showNavigationUI() {
        with(binding) {
            appBar.prepareShow(appBar.height).start()
            bottomNavigation.prepareSlideUp(bottomNavigation.height, startDelay = 150).start()
        }
    }
}