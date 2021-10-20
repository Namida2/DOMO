package tools


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.domo.R
import com.example.domo.databinding.DialogErrorBinding
import com.example.domo.views.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class MyAlertDialog<T>(
    private val binding: DialogErrorBinding,
    private val title: String,
    private val message: String,
    private val action: (() -> T)? = null
) : DialogFragment() {

    private val DISMISS_TIME: Long = 140

    companion object {
        val isExist: AtomicBoolean = AtomicBoolean(false)
        fun <T> getNewInstance(
            binding: DialogErrorBinding,
            title: String,
            message: String,
            action: (() -> T)? = null
        ): MyAlertDialog<T>? {
            if (isExist.get()) {
                log("Tools.ErrorAlertDialog:: dialog already exists.")
                return null
            }
            return MyAlertDialog(binding, title, message, action)
        }

    }
    override fun onAttach(context: Context) {
        isExist.set(true)
        super.onAttach(context)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
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
        return builder.setView(binding.root)
            .setCancelable(false).create()
    }
    override fun onDestroyView() {
        (binding.root.parent as ViewGroup).removeView(binding.root)
        super.onDestroyView()
    }

    override fun onDetach() {
        isExist.set(false)
        super.onDetach()
    }
}

