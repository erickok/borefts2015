package nl.brouwerijdemolen.borefts2013.gui

import arrow.core.Try
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Unconfined
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
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.log.EmptyLogger
import org.koin.standalone.StandAloneContext
import kotlin.coroutines.CoroutineContext

val dummyBrewer = Brewer(2, "logo", "brewerName", "brwNm", "brewer name", "Leuven", "Belgium", null, null, 0f, 0f)
val dummyStyle = Style(3, "styleName", 3, 1, 2, 3, 4, 5)
val dummyBeer = Beer(1, "beerName", 2, 3, 5f, false, true, null, -1, 0, 1, 2, 3, 4, 5, dummyBrewer, dummyStyle)
val dummyPoint = Point(1f, 2f)
val dummyArea = Area("areaId", "bier", "beer", "black", listOf(dummyPoint))
val dummyPoi = Poi("poiId", "plaats", "place", "poiMarker", dummyPoint)
val dummyVersion = Version(60)
val dummyBrewers = Brewers(listOf(dummyBrewer), dummyVersion)
val dummyStyles = Styles(listOf(dummyStyle), dummyVersion)
val dummyBeers = Beers(listOf(dummyBeer), dummyVersion)
val dummyPois = Pois(listOf(dummyArea), listOf(dummyPoi), 60)

val testModule = module {
    single("ui") { Unconfined as CoroutineContext }
    single { StubApi() as Api }
    single { MemoryCache(Long.MAX_VALUE) }
    single { Repository(get(), get()) }
    single { mock<StarPersistence>() }
    single { mock<Navigator>() }
}

object TestWithKoin {

    fun startKoin(modules: List<Module> = listOf(testModule, viewModelsModule)) {
        StandAloneContext.startKoin(modules, logger = EmptyLogger())
    }

    fun stopKoin() {
        StandAloneContext.closeKoin()
    }
}

class StubApi : Api {
    override suspend fun pois() = Try.just(dummyPois)
    override suspend fun brewers() = Try.just(dummyBrewers)
    override suspend fun styles() = Try.just(dummyStyles)
    override suspend fun beersRaw() = Try.just(dummyBeers)
}
