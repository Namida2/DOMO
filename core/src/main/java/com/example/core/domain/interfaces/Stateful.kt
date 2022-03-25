package com.example.core.domain.interfaces

interface Stateful<S> {
    fun setNewState(state: S)
}