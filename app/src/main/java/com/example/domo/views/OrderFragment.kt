package com.example.domo.views

import android.R.transition
import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.transition.doOnEnd
import androidx.core.transition.doOnStart
import androidx.fragment.app.Fragment
import com.example.domo.R
import com.example.domo.databinding.FragmentOrderBinding
import tools.Animations

class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.transition_fragnet_order).apply {
            doOnEnd {
                Animations.showView(binding.toolbar, 250).start()
            }
        }
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.transition_fragnet_order).apply {
            doOnStart {
                Animations.hideView(binding.toolbar, 5000).start()
            }
        }

        // TODO: add transition with recyclerView
        enterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_bottom)
        return binding.root
    }


}