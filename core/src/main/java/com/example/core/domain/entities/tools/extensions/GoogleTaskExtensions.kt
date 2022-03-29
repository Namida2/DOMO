package com.example.core.domain.entities.tools.extensions

import com.google.android.gms.tasks.Task

fun <TResult, A, R, T : com.example.core.domain.entities.tools.Task<A, R>>
        Task<TResult>.addOnSuccessListenerWithDefaultFailureHandler(
    task: T, onSuccessBlock: (Task<TResult>) -> Unit
) {
    this.addOnSuccessListener { onSuccessBlock(this) }
    this.addOnFailureListener { task.onError(it.getExceptionMessage()) }
}