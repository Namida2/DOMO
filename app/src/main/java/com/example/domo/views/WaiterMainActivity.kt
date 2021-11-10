package com.example.domo.views

import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.domo.R
import com.example.domo.databinding.ActivityWaiterMainBinding
import extentions.Animations.prepareHide
import extentions.Animations.prepareShow
import extentions.Animations.prepareSlideDown
import extentions.Animations.prepareSlideUp

class WaiterMainActivity : AppCompatActivity(),
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
                bottomNavigation.prepareSlideDown(bottomNavigation.height).apply {
                    doOnEnd {
                        bottomAppBar.prepareSlideUp(bottomAppBar.height).apply {
                            doOnEnd { menuFba.show() }
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




