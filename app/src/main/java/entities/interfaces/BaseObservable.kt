package entities.interfaces

interface BaseObservable<Subscriber> {
    fun subscribe(subscriber: Subscriber)
    fun unSubscribe(subscriber: Subscriber)
    fun notifyChanges()
}