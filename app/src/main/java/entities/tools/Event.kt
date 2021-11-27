package entities.tools

class Event<T>(
    private val data: T,
) {
    private var handled = false
    fun getData(): T? =
        when (handled) {
            true -> null
            else -> { handled = true; data }
        }
}

