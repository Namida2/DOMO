package com.example.domo.views.activities

import android.graphics.Rect
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.domo.R
import com.example.domo.databinding.ActivityWaiterMainBinding

import com.example.domo.viewModels.ViewModelFactory
import com.example.domo.viewModels.shared.WaiterActOrderFragSharedViewModel
import database.daos.OrderDao
import entities.order.Order
import entities.order.OrderInfo
import entities.order.OrderItem
import extentions.Animations.prepareHide
import extentions.Animations.prepareShow
import extentions.Animations.prepareSlideDown
import extentions.Animations.prepareSlideUp
import extentions.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class WaiterMainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityWaiterMainBinding
    private var navController: NavController? = null
    private val sharedViewModel: WaiterActOrderFragSharedViewModel by viewModels {
        ViewModelFactory(appComponent)
    }

    @Inject
    lateinit var orderDao: OrderDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityWaiterMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        binding.lifecycleOwner = this
//        binding.sharedViewModel = sharedViewModel
//
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
//        navController = navHostFragment.navController.apply {
//            addOnDestinationChangedListener(this@WaiterMainActivity)
//        }

        appComponent.inject(this)
        CoroutineScope(IO).launch {
            insert()
        }
    }

    suspend fun insert() {
        val order1 = Order(OrderInfo(1, 1), getListOrderItems1().toMutableSet())
        val order2 = Order(OrderInfo(2, 1), getListOrderItems2().toMutableSet())
        orderDao.insert(order1)
        orderDao.insert(order2)
        val readOrder = orderDao.readOrder(1)
        log(readOrder.toString())
        orderDao.deleteOrder(1)
    }

    private fun getListOrderItems1(): List<OrderItem> = listOf(
        OrderItem(1, 1, 1, "aaa"),
        OrderItem(1, 2, 1, "aaa"),
        OrderItem(1, 3, 1, "aaa"),
        OrderItem(1, 4, 1, "aaa"),
        OrderItem(1, 5, 1, "aaa"),
        OrderItem(1, 6, 1, "aaa"),
    )
    private fun getListOrderItems2(): List<OrderItem> = listOf(
        OrderItem(2, 1, 1, "xxx"),
        OrderItem(2, 2, 1, "xxx"),
        OrderItem(2, 3, 1, "xxx"),
        OrderItem(2, 4, 1, "xxx"),
        OrderItem(2, 5, 1, "xxx"),
        OrderItem(2, 6, 1, "xxx"),
    )


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

    private fun hideNavigationUI() {
        with(binding) {
            appBar.prepareHide(appBar.height).start()
            val scrollBounds = Rect()
            rootCoordinationLayout.getHitRect(scrollBounds)
            if (bottomNavigation.getLocalVisibleRect(scrollBounds)) {
                bottomNavigation.prepareSlideDown(bottomNavigation.height).apply {
                    doOnEnd {
                        bottomAppBar.prepareSlideUp(bottomAppBar.height).apply {
                            doOnStart { menuFba.show() }
                        }.start()
                    }
                }.start()
            } else {
                bottomAppBar.prepareSlideUp(bottomAppBar.height).apply {
                    doOnEnd { menuFba.show() }
                }.start()
            }
        }
    }

    private fun showNavigationUI() {
        with(binding) {
            appBar.prepareShow(appBar.height).start()
            bottomAppBar.prepareSlideDown(bottomAppBar.height).apply {
                doOnEnd { bottomNavigation.prepareSlideUp(bottomNavigation.height).start() }
            }.start()
            menuFba.hide()
        }
    }
}




