package nl.brouwerijdemolen.borefts2013.gui

import arrow.core.Try
import arrow.core.fix
import arrow.core.extensions.`try`.monad.binding
import nl.brouwerijdemolen.borefts2013.api.Api
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Pois

class Repository(private val api: Api, private val memoryCache: MemoryCache) {

    suspend fun poisAndAreas(): Try<Pois> = memoryCache.cachedPois.getFreshOr { api.pois() }

    suspend fun pois() = poisAndAreas().map { it.pois }

    suspend fun areas() = poisAndAreas().map { it.areas }

    suspend fun brewers() = memoryCache.cachedBrewers.getFreshOr { api.brewers() }.map { it.brewers }

    suspend fun styles() = memoryCache.cachedStyles.getFreshOr { api.styles() }.map { it.styles }

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

