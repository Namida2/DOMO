package tools.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.domo.R
import com.example.domo.databinding.DialogProcessBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tools.Animations

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
        Animations.hideView(binding?.loadingLinearLayout!!)
        CoroutineScope(Main).launch {
            delay(500)
            dismiss()
        }
    }

}