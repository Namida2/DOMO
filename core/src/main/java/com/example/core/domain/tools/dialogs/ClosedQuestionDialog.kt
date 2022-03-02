package com.example.core.domain.tools.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.core.R
import com.example.core.databinding.DialogClodsedQuestionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ClosedQuestionDialog<A>(
    private val title: String? = null,
    private val message: String? = null,
    private val onAccept: (arg: A?) -> Unit
) : DialogFragment() {

    var arg: A? = null

    @SuppressLint("ResourceType")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogClodsedQuestionBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context, R.style.alertDialogStyle)
        with(binding) {
            this@ClosedQuestionDialog.title?.let { title.text = it }
            this@ClosedQuestionDialog.message?.let { message.text = it }
            cancelButton.setOnClickListener {
                myDismiss()
            }
            acceptButton.setOnClickListener {
                onAccept.invoke(arg)
                myDismiss()
            }
        }
        isCancelable = false
        return builder.setView(binding.root).create()
    }

    private fun myDismiss() {
        CoroutineScope(Main).launch {
            delay(requireContext().resources.getInteger(R.integer.dismissDialogTime).toLong())
            dismiss()
        }
    }
}