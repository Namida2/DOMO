package com.example.core.domain.entities.tools.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.core.domain.entities.tools.dialogs.ProcessAlertDialog

fun DialogFragment.showIfNotAdded(fragmentManager: FragmentManager, tag: String) {
    if(!this.isAdded) this.show(fragmentManager, tag)
    else logD("$this: already added.")
}

fun DialogFragment.dismissIfAdded() {
    if (this.isAdded) this.dismiss()
}