package Tools

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.domo.R
import com.example.domo.databinding.DialogErrorBinding
import android.content.DialogInterface

import android.R




class ErrorAlertDialog<T>(
    private val binding: DialogErrorBinding,
    private val title: String,
    private val message: String,
    private val action: () -> T) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        with(binding) {
            titleTextView.text = title
            messageTextView.text = message
            actionButton.setOnClickListener {
                action()
            }
        }

        return super.onCreateDialog(savedInstanceState)
    }
}

class MyAlertDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments!!.getInt("title")
        return AlertDialog.Builder(activity)
            .setIcon(R.drawable.alert_dialog_icon)
            .setTitle(title)
            .setPositiveButton(R.string.alert_dialog_ok,
                DialogInterface.OnClickListener { dialog, whichButton -> (activity as FragmentAlertDialog?).doPositiveClick() }
            )
            .setNegativeButton(R.string.alert_dialog_cancel,
                DialogInterface.OnClickListener { dialog, whichButton -> (activity as FragmentAlertDialog?).doNegativeClick() }
            )
            .create()
    }

    companion object {
        fun newInstance(title: Int): MyAlertDialogFragment {
            val frag = MyAlertDialogFragment()
            val args = Bundle()
            args.putInt("title", title)
            frag.arguments = args
            return frag
        }
    }
}