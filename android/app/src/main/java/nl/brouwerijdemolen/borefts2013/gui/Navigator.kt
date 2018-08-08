package nl.brouwerijdemolen.borefts2013.gui

import android.content.Context
import android.content.Intent
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
        startTask(BrewerActivity(appContext, brewer = brewer))
    }

    override fun openStyle(style: Style) {
        startTask(StyleActivity(appContext, style = style))
    }

    override fun openBeer(beer: Beer) {
        startTask(BeerActivity(appContext, beer = beer))
    }

    override fun openMap() {
        startTask(MapActivity(appContext))
    }

    override fun openMapForBrewer(brewerId: Int) {
        startTask(MapActivity(appContext, focusBrewerId = brewerId))
    }

    override fun openMapForPoi(poiId: String) {
        startTask(MapActivity(appContext, focusPoiId = poiId))
    }

    private fun startTask(target: Intent) {
        appContext.startActivity(target.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

}
