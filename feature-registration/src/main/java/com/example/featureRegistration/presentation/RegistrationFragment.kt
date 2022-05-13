package com.example.featureRegistration.presentation

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.constants.OtherStringConstants
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.entities.tools.extensions.dismissIfAdded
import com.example.core.domain.entities.tools.extensions.isNetworkConnected
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.core.presentation.ProcessAleartDialog.ProcessAlertDialog
import com.example.core.presentation.adminPasswordDialog.AdminPasswordDialog
import com.example.featureRegistration.R
import com.example.featureRegistration.databinding.FragmentRegistrationBinding
import com.example.featureRegistration.domain.ViewModelFactory
import com.example.featureRegistration.domain.recyclerView.PostItemDecoration
import com.example.featureRegistration.domain.recyclerView.PostItemsAdapter

class RegistrationFragment : Fragment() {

    private var smallMargin: Int? = null
    private var largeMargin: Int? = null
    private lateinit var employeeAuthCallback: EmployeeAuthCallback
    private val viewModel by viewModels<RegistrationViewModel> { ViewModelFactory }
    lateinit var binding: FragmentRegistrationBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        employeeAuthCallback =
            (context as? EmployeeAuthCallback) ?: throw IllegalArgumentException(
                OtherStringConstants.ACTIVITY_IS_NOT_EMPLOYEE_AUTHORIZATION_CALLBACK
            )
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
        initBindings(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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
        with(binding.postsRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = PostItemsAdapter(getPostImagesList(), viewModel.getPostItems()) { post ->
                viewModel.selectedPost = post
            }
            addItemDecoration(PostItemDecoration(smallMargin!!, largeMargin!!))
        }
        binding.registrationButton.setOnClickListener {
            if (requireContext().isNetworkConnected())
                with(binding) {
                    viewModel.validation(
                        nameEditText.text.toString(),
                        //Important thing
                        emailEditText.text.toString().lowercase(),
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
        binding.toolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun observeViewModelState() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is RegistrationVMStates.Validating -> {
                    ProcessAlertDialog.showIfNotAdded(parentFragmentManager, "")
                }
                is RegistrationVMStates.Valid -> {
                    employeeAuthCallback.onAuthorisationEvent(it.employee)
                }
                is RegistrationVMStates.OnFailure -> {
                    requireContext().createMessageDialog(it.errorMessage) {
                        ProcessAlertDialog.dismissIfAdded()
                    }?.show(parentFragmentManager, "")
                }
                is RegistrationVMStates.RequestPassword -> {
                    ProcessAlertDialog.dismissIfAdded()
                    AdminPasswordDialog(it.correctPassword, viewModel)
                        .show(parentFragmentManager, "")
                }
                is RegistrationVMStates.Default -> {
                    ProcessAlertDialog.dismissIfAdded()
                }
            }
        }
    }

    private fun getPostImagesList(): List<BitmapDrawable> = listOf(
        BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_cook)),
        BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_waiter)),
        BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_administrator))
    )
}