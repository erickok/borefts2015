package nl.brouwerijdemolen.borefts2013.gui

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.Try
import arrow.core.none
import arrow.core.some
import nl.brouwerijdemolen.borefts2013.api.Beers
import nl.brouwerijdemolen.borefts2013.api.Brewers
import nl.brouwerijdemolen.borefts2013.api.Pois
import nl.brouwerijdemolen.borefts2013.api.Styles

class MemoryCache(maxAgeInMillis: Long) {

    val cachedPois = Cached<Try<Pois>>(maxAgeInMillis)
    val cachedBrewers = Cached<Try<Brewers>>(maxAgeInMillis)
    val cachedStyles = Cached<Try<Styles>>(maxAgeInMillis)
    val cachedBeers = Cached<Try<Beers>>(maxAgeInMillis)

}

class Cached<T>(private val maxAgeInMillis: Long) {

    var value: Option<T> = none()
        private set(value) {
            timestamp = System.currentTimeMillis()
            field = value
        }
    private var timestamp: Long = 0L

    val isFresh: Boolean get() = (System.currentTimeMillis() - timestamp) < maxAgeInMillis

    suspend fun getFreshOr(refresh: suspend () -> T): T {
        val value = this.value
        return when (value) {
            None -> refreshAndCache(refresh)
            is Some -> if (isFresh) value.t else refreshAndCache(refresh)
        }
    }

    private suspend fun refreshAndCache(refresh: suspend () -> T): T {
        return refresh().also { this.value = it.some() }
    }

}
