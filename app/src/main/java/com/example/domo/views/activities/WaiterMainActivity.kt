package com.example.domo.views.activities

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import extentions.Animations.prepareHide
import extentions.Animations.prepareShow
import extentions.Animations.prepareSlideDown
import extentions.Animations.prepareSlideUp
import javax.inject.Inject

class WaiterMainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityWaiterMainBinding
    private var navController: NavController? = null
    private val sharedViewModel: WaiterActOrderFragSharedViewModel by viewModels { ViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaiterMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

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




