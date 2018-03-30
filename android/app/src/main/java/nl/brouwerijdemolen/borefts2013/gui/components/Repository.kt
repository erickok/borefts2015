package nl.brouwerijdemolen.borefts2013.gui.components

import nl.brouwerijdemolen.borefts2013.api.Api

// TODO Implement persistent caching
class Repository(private val api: Api) {

    suspend fun pois() = api.pois().pois

    suspend fun areas() = api.pois().areas

    suspend fun brewers() = api.brewers().brewers

    suspend fun styles() = api.styles().styles

    suspend fun brewerBeers(brewerId: Int) = api.beers().beers.filter { it.brewerId == brewerId }

    suspend fun styleBeers(styleId: Int) = api.beers().beers.filter { it.styleId == styleId }

}
