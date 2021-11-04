package com.example.domo.views

import android.R.transition
import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(
            transition.move)
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(
            transition.move)
        // TODO: add different animations for view using ViewTreeObserver
        binding.bottomAppBar.someThing
        return binding.root
    }

}