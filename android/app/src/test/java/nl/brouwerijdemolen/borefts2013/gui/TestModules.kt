package nl.brouwerijdemolen.borefts2013.gui

import arrow.core.Try
import kotlinx.coroutines.experimental.Unconfined
import nl.brouwerijdemolen.borefts2013.api.Api
import nl.brouwerijdemolen.borefts2013.api.Area
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Beers
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.Brewers
import nl.brouwerijdemolen.borefts2013.api.Poi
import nl.brouwerijdemolen.borefts2013.api.Point
import nl.brouwerijdemolen.borefts2013.api.Pois
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.api.Styles
import nl.brouwerijdemolen.borefts2013.api.Version
import nl.brouwerijdemolen.borefts2013.gui.components.StarPersistence
import nl.brouwerijdemolen.borefts2013.mock
import org.koin.dsl.module.module
import kotlin.coroutines.experimental.CoroutineContext

val mockNavigator: Navigator = mock()
val mockStarPersistence: StarPersistence = mock()

val testModule = module(override = true) {
    single("ui") { Unconfined as CoroutineContext }
    single { StubApi() as Api }
    single { MemoryCache(Long.MAX_VALUE) }
    single { Repository(get(), get()) }
    single { mockStarPersistence }
    single { mockNavigator }
}

val dummyBrewer = Brewer(2, "logo", "brewerName", "brwNm", "brewer name", "Leuven", "Belgium", null, null, 0f, 0f)
val dummyStyle = Style(3, "styleName", 3, 1, 2, 3, 4, 5)
val dummyBeer = Beer(1, "beerName", 2, 3, 5f, false, true, null, -1, 0, 1, 2, 3, 4, 5, dummyBrewer, dummyStyle)
val dummyPoint = Point(1f, 2f)
val dummyArea = Area("areaId", "bier", "beer", "black", listOf(dummyPoint))
val dummyPoi = Poi("poiId", "plaats", "place", "poiMarker", dummyPoint)
val dummyVersion = Version(60)

class StubApi : Api {

    override suspend fun pois() = Try.just(Pois(listOf(dummyArea), listOf(dummyPoi)))

    override suspend fun brewers() = Try.just(Brewers(listOf(dummyBrewer), dummyVersion))

    override suspend fun styles() = Try.just(Styles(listOf(dummyStyle), dummyVersion))

    override suspend fun beersRaw() = Try.just(Beers(listOf(dummyBeer), dummyVersion))

}
