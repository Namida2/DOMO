package com.example.waiterCore.domain.tools.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.waiterCore.R
import com.example.waiterCore.databinding.DialogMessageBinding
import com.example.waiterCore.domain.tools.extensions.logD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class MessageAlertDialog(
    private val title: String,
    private val message: String,
    private val action: (() -> Unit)? = null,
) : DialogFragment() {

    private val DISMISS_TIME: Long = 140

    companion object {
        val isExist: AtomicBoolean = AtomicBoolean(false)
        fun <T> getNewInstance(
            title: String,
            message: String,
            action: (() -> Unit)? = null,
        ): MessageAlertDialog? {
            if (isExist.get()) {
                logD("$this: Dialog already exists.")
                return null
            }
            return MessageAlertDialog(title, message, action)
        }
    }

    override fun onAttach(context: Context) {
        isExist.set(true)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val asd = layoutInflater
        val binding = DialogMessageBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context, R.style.alertDialogStyle)
        with(binding) {
            titleTextView.text = title
            messageTextView.text = message
            actionButton.setOnClickListener {
                action?.let { notNullAction -> notNullAction() }
                CoroutineScope(Main).launch {
                    delay(DISMISS_TIME)
                    dismiss()
                }
            }
        }
        isCancelable = false
        return builder.setView(binding.root).create()
    }

    override fun onDetach() {
        isExist.set(false)
        super.onDetach()
    }
}

