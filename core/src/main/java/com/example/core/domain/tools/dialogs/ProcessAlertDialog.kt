package com.example.core.domain.tools.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.core.R
import com.example.core.databinding.DialogProcessBinding
import com.example.core.domain.tools.extensions.Animations.prepareHide
import com.example.core.domain.tools.extensions.Animations.prepareShow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ProcessAlertDialog : DialogFragment() {

    private var binding: DialogProcessBinding? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogProcessBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext(), R.style.alertDialogStyle)
        isCancelable = false
        return builder.setView(binding?.root)
            .create()
    }

    fun onSuccess() {
        binding?.loadingLinearLayout!!.prepareHide().start()
        binding?.successTextView!!.prepareShow(startDelay = 150).start()
        CoroutineScope(Main).launch {
            delay(600)
            dismiss()
        }
    }
}