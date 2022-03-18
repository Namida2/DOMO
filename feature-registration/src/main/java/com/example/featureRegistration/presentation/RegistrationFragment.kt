package com.example.featureRegistration.presentation

import android.content.Context
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
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.core.domain.tools.constants.OtherStringConstants
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.core.domain.tools.extensions.isNetworkConnected
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
                    viewModel.validation(
                        nameEditText.text.toString(),
                        //TODO: Important thing
                        emailEditText.text.toString().lowercase(),
                        passwordEditText.text.toString(),
                        confirmPasswordEditText.text.toString(),
                    )
                }
            else requireContext().createMessageDialog(
                com.example.core.domain.tools.ErrorMessage(
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
                    com.example.core.domain.tools.dialogs.ProcessAlertDialog.show(
                        parentFragmentManager,
                        ""
                    )
                }
                is RegistrationViewModelStates.Valid -> {
                    employeeAuthCallback.onEmployeeLoggedIn(it.employee)
                }
                else -> {
                    if (it is RegistrationViewModelStates.Default) return@observe
                    com.example.core.domain.tools.dialogs.ProcessAlertDialog.dismiss()
                    dialog = requireContext().createMessageDialog(it.errorMessage!!)
                }
            }
            dialog?.show(parentFragmentManager, "")
        }
    }
}