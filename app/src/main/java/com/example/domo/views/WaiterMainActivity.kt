package com.example.domo.views

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.transition.TransitionManager
import com.example.domo.R
import com.example.domo.databinding.ActivityWaiterMainBinding
import com.google.android.material.transition.MaterialContainerTransform
import extentions.Animations.prepareShow
import extentions.Animations.prepareHide

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
        //TODO: Add shared ViewModel
        //TODO: Study "8. Add Container Transform transition from email address chip to card view" in codeLabs
        binding.menuFloatingActionButton.setOnClickListener{
            val transform = MaterialContainerTransform().apply {
                startView = binding.aaa
                endView = binding.container
                scrimColor = Color.TRANSPARENT
                duration = 300
                addTarget(binding.container)
            }
            TransitionManager.beginDelayedTransition(binding.rootCoordinationLayout, transform)
            binding.container.visibility = View.VISIBLE
            binding.aaa.visibility = View.INVISIBLE
        }
        binding.target.setOnClickListener{
           val transform = MaterialContainerTransform().apply {
               startView = binding.container
               endView = binding.aaa
               duration = 300
               scrimColor = Color.TRANSPARENT
               addTarget(binding.aaa)
           }
            TransitionManager.beginDelayedTransition(binding.rootCoordinationLayout, transform)
            binding.aaa.visibility = View.VISIBLE
            binding.container.visibility = View.INVISIBLE
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.orderFragment -> {
                with(binding) {
                    appBar.prepareHide( appBar.height.toFloat(), ).start()
                }

            }
            R.id.tablesFragment -> {
                with(binding) {
                    appBar.prepareShow( appBar.height.toFloat()).start()
                }
            }
        }
    }
}