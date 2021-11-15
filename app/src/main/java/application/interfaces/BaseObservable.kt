package application.interfaces

interface BaseObservable<Observer> {
    fun addObserver(observer: Observer)
    fun deleteObserver(observer: Observer)
    fun notifyObservers()
}