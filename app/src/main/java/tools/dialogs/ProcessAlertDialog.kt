package tools.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.domo.R
import com.example.domo.databinding.DialogProcessBinding
import com.example.domo.views.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tools.Animations
import java.util.concurrent.atomic.AtomicBoolean

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
        Animations.hideView(binding?.loadingLinearLayout!!).start()
        Animations.showView(binding?.successTextView!!, startDelay = 150).start()
        CoroutineScope(Main).launch {
            delay(600)
            dismiss()
        }
    }


}