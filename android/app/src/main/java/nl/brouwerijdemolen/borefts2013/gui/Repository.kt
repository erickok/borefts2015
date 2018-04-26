package nl.brouwerijdemolen.borefts2013.gui

import arrow.data.Try
import arrow.data.ev
import arrow.data.monad
import arrow.typeclasses.binding
import nl.brouwerijdemolen.borefts2013.api.Api
import nl.brouwerijdemolen.borefts2013.api.Beer

// TODO Implement persistent caching
class Repository(private val api: Api) {

    suspend fun pois() = api.pois().map { it.pois }

    suspend fun areas() = api.pois().map { it.areas }

    suspend fun brewers() = api.brewers().map { it.brewers }

    suspend fun styles() = api.styles().map { it.styles }

    private suspend fun allBeers(): Try<List<Beer>> {
        val tryBrewers = api.brewers()
        val tryStyles = api.styles()
        val tryBeers = api.beersRaw()
        return Try.monad().binding {
            val brewers = tryBrewers.bind()
            val styles = tryStyles.bind()
            val beersRaw = tryBeers.bind()
            beersRaw.beers.onEach { beer ->
                beer.brewer = brewers.brewers.single { it.id == beer.brewerId }
                beer.style = styles.styles.single { it.id == beer.styleId }
            }
        }.ev()
    }

    suspend fun brewerBeers(brewerId: Int) = allBeers().map { it.filter { it.brewerId == brewerId } }

    suspend fun styleBeers(styleId: Int) = allBeers().map { it.filter { it.styleId == styleId } }

    suspend fun someBeers(beerIds: Set<Int>) = allBeers().map { it.filter { beerIds.contains(it.id) } }

    suspend fun beer(beerId: Int) = allBeers().map { it.single { it.id == beerId } }

}

