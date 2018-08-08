package nl.brouwerijdemolen.borefts2013.gui

import android.content.Context
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.screens.BeerActivity
import nl.brouwerijdemolen.borefts2013.gui.screens.BrewerActivity
import nl.brouwerijdemolen.borefts2013.gui.screens.MapActivity
import nl.brouwerijdemolen.borefts2013.gui.screens.StyleActivity

interface Navigator {

    fun openBrewer(brewer: Brewer)

    fun openStyle(style: Style)

    fun openBeer(beer: Beer)

    fun openMap()

    fun openMapForBrewer(brewerId: Int)

    fun openMapForPoi(poiId: String)

}

class IntentNavigator(private val appContext: Context) : Navigator {

    override fun openBrewer(brewer: Brewer) {
        appContext.startActivity(BrewerActivity(appContext, brewer = brewer))
    }

    override fun openStyle(style: Style) {
        appContext.startActivity(StyleActivity(appContext, style = style))
    }

    override fun openBeer(beer: Beer) {
        appContext.startActivity(BeerActivity(appContext, beer = beer))
    }

    override fun openMap() {
        appContext.startActivity(MapActivity(appContext))
    }

    override fun openMapForBrewer(brewerId: Int) {
        appContext.startActivity(MapActivity(appContext, focusBrewerId = brewerId))
    }

    override fun openMapForPoi(poiId: String) {
        appContext.startActivity(MapActivity(appContext, focusPoiId = poiId))
    }

}
