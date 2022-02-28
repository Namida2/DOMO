package com.example.core.domain.interfaces

interface BaseObservable<Subscriber> {
    fun subscribe(subscriber: Subscriber)
    fun unSubscribe(subscriber: Subscriber)
    fun notifyChanges()
}