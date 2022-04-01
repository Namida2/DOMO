package com.example.core.domain.entities.tools.extensions

import android.widget.TextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun TextView.precomputeAndSetText(text: String) {
    val params: PrecomputedTextCompat.Params =
        TextViewCompat.getTextMetricsParams(this)
    CoroutineScope(Unconfined).launch {
        val precomputedText =
            PrecomputedTextCompat.create(text, params)
        withContext(Main) {
            TextViewCompat.setPrecomputedText(this@precomputeAndSetText, precomputedText)
        }
    }
}