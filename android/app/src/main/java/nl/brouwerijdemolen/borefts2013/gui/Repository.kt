package nl.brouwerijdemolen.borefts2013.gui

import arrow.core.Try
import arrow.core.extensions.`try`.monad.binding
import arrow.core.fix
import nl.brouwerijdemolen.borefts2013.api.Api
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Pois
import nl.brouwerijdemolen.borefts2013.api.Style
import kotlin.math.roundToInt

class Repository(private val api: Api, private val memoryCache: MemoryCache) {

    suspend fun poisAndAreas(): Try<Pois> = memoryCache.cachedPois.getFreshOr { api.pois() }

    suspend fun pois() = poisAndAreas().map { it.pois }

    suspend fun areas() = poisAndAreas().map { it.areas }

    suspend fun brewers() = memoryCache.cachedBrewers.getFreshOr { api.brewers() }.map { it.brewers }

    suspend fun styles(): Try<List<Style>> {
        val tryStyles = memoryCache.cachedStyles.getFreshOr { api.styles() }.map { it.styles }
        val tryBeers = memoryCache.cachedBeers.getFreshOr { api.beersRaw() }
        return binding {
            val styles = tryStyles.bind()
            val beersRaw = tryBeers.bind()
            styles.asSequence()
                    // Filter out styles without beers
                    .filter { style -> beersRaw.beers.any { it.styleId == style.id } }
                    // Calculate avg color and flavour from style's beers
                    .map { style ->
                        val styleBeers = beersRaw.beers.filter { it.styleId == style.id }
                        val avgAbv = styleBeers.average { it.abvIndication }
                        val avgColor = styleBeers.average { it.colorInt }
                        val avgBitterness = styleBeers.average { it.bitter }
                        val avgSweet = styleBeers.average { it.sweet }
                        val avgSour = styleBeers.average { it.sour }
                        style.copy(color = avgColor, abv = avgAbv, bitterness = avgBitterness, sweetness = avgSweet, acidity = avgSour)
                    }
                    .toList()
        }.fix()
    }

    private fun <T> List<T>.average(value: (T) -> Int): Int {
        return (this.sumBy(value).toFloat() / this.count()).roundToInt().coerceIn(1, 5)
    }

    private suspend fun allBeers(): Try<List<Beer>> {
        val tryBrewers = brewers()
        val tryStyles = styles()
        val tryBeers = memoryCache.cachedBeers.getFreshOr { api.beersRaw() }
        return binding {
            val brewers = tryBrewers.bind()
            val styles = tryStyles.bind()
            val beersRaw = tryBeers.bind()
            beersRaw.beers.onEach { beer ->
                // TODO fix back to single {} when the data set is fixed
                beer.brewer = brewers.last { it.id == beer.brewerId }
                beer.style = styles.last { it.id == beer.styleId }
            }
        }.fix()
    }

    suspend fun brewerBeers(brewerId: Int) = allBeers().map { it.filter { it.brewerId == brewerId } }

    suspend fun styleBeers(styleId: Int) = allBeers().map { it.filter { it.styleId == styleId } }

    suspend fun someBeers(beerIds: Set<Int>) = allBeers().map { it.filter { beerIds.contains(it.id) } }

    suspend fun beer(beerId: Int) = allBeers().map { it.single { it.id == beerId } }

}

