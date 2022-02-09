package com.example.waiterMain.presentation

import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.waiterCore.domain.tools.extensions.Animations.prepareHide
import com.example.waiterCore.domain.tools.extensions.Animations.prepareShow
import com.example.waiterCore.domain.tools.extensions.Animations.prepareSlideDown
import com.example.waiterCore.domain.tools.extensions.Animations.prepareSlideUp
import com.example.waiterMain.R
import com.example.waiterMain.databinding.ActivityWaiterMainBinding

//TODO: Change the order fragment layout and implement the order fragment feature
class WaiterMainActivity: AppCompatActivity(),
    NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityWaiterMainBinding
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaiterMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController.apply {
            addOnDestinationChangedListener(this@WaiterMainActivity)
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