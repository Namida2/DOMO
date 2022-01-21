package com.example.domo.views.fragments.authorisation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domo.R
import com.example.domo.adapters.PostItemsAdapter
import com.example.domo.adapters.itemDecorations.PostItemDecoration
import com.example.domo.databinding.FragmentRegistrationBinding
import com.example.domo.splashScreen.presentation.SplashScreenActivity
import com.example.domo.viewModels.RegistrationViewModel
import com.example.domo.viewModels.RegistrationViewModelStates
import com.example.domo.viewModels.ViewModelFactory
import com.example.waiter_core.domain.tools.ErrorMessage
import extentions.createMessageDialog
import extentions.isNetworkConnected
import tools.dialogs.ProcessAlertDialog

class RegistrationFragment : Fragment() {

    private var smallMargin: Int? = null
    private var largeMargin: Int? = null

    private val viewModel: RegistrationViewModel by viewModels { ViewModelFactory }
    lateinit var binding: FragmentRegistrationBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.resetState()
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
        initBindings(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding.lifecycleOwner = viewLifecycleOwner
        observeViewModelState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun initBindings(inflater: LayoutInflater) {
        binding = FragmentRegistrationBinding.inflate(inflater)
        binding.viewModel = viewModel

        with(binding.postsRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = viewModel.getPostItems().let {
                PostItemsAdapter(it) { post ->
                    viewModel.selectedPost = post
                }
            }
            addItemDecoration(
                PostItemDecoration(
                    smallMargin!!,
                    largeMargin!!
                )
            )
        }
        binding.registrationButton.setOnClickListener {
            if (requireContext().isNetworkConnected())
                with(binding) {
                    viewModel?.validation(
                        nameEditText.text.toString(),
                        emailEditText.text.toString(),
                        passwordEditText.text.toString(),
                        confirmPasswordEditText.text.toString(),
                    )
                }
            else requireContext().createMessageDialog(
                ErrorMessage(
                    R.string.defaultTitle,
                    R.string.networkConnectionMessage
                )
            )?.show(parentFragmentManager, "")
        }
    }

    private fun observeViewModelState() {
        viewModel.state.observe(viewLifecycleOwner) {
            var dialog: DialogFragment? = null
            when (it) {
                is RegistrationViewModelStates.Validating -> {
                    ProcessAlertDialog.show(parentFragmentManager, "")
                }
                is RegistrationViewModelStates.Valid -> {
                    //Can not perform this action after onSaveInstanceState (it's about dismiss)
                    //ProcessAlertDialog.onSuccess()
                    startActivity(Intent(requireContext(), SplashScreenActivity::class.java))
                }
                else -> {
                    if (it is RegistrationViewModelStates.Default) return@observe
                    ProcessAlertDialog.dismiss()
                    dialog = requireContext().createMessageDialog(it.errorMessage!!)
                }
            }
            dialog?.show(parentFragmentManager, "")
        }
    }

}

