package nl.brouwerijdemolen.borefts2013.test

import android.arch.lifecycle.Observer

/**
 * A [android.arch.lifecycle.LiveData] [Observer] that registers all emitted values. This is useful in tests to validate the amount and content of
 * all emissions.
 */
class Register<T> : Observer<T> {

    private val registered = mutableListOf<T?>()

    /**
     * The registered values emitted by the observed [android.arch.lifecycle.LiveData].
     */
    val values: List<T?> get() = registered

    /**
     * The last value emitted by the observed [android.arch.lifecycle.LiveData], or null if nothing was emitted.
     */
    val lastValue: T? get() = values.getOrNull(values.size - 1)

    /**
     * The amount of values emitted by the observed [android.arch.lifecycle.LiveData].
     */
    val valueCount: Int get() = values.size

    override fun onChanged(t: T?) {
        registered.add(t)
    }

}
