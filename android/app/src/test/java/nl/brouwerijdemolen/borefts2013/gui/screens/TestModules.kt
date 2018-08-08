package nl.brouwerijdemolen.borefts2013.gui.screens

import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.components.StarPersistence
import nl.brouwerijdemolen.borefts2013.mock
import org.koin.dsl.module.module

val mockNavigator: Navigator = mock()
val starPersistence: StarPersistence = mock()

val testModule = module(override = true) {
    single { starPersistence }
    single { mockNavigator }
}

val dummyBrewer = Brewer(2, "logo", "brewerName", "brwNm", "brewer name", "Leuven", "Belgium", null, null, 0f, 0f)
val dummyStyle = Style(3, "styleName", 3, 1, 2, 3, 4, 5)
val dummyBeer = Beer(1, "beerName", 2, 3, 5f, false, true, null, -1, 0, 1, 2, 3, 4, 5, dummyBrewer, dummyStyle)
