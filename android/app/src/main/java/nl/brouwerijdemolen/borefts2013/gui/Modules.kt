package nl.brouwerijdemolen.borefts2013.gui

import nl.brouwerijdemolen.borefts2013.api.Api
import nl.brouwerijdemolen.borefts2013.gui.components.AppRater
import nl.brouwerijdemolen.borefts2013.gui.components.ResourceProvider
import nl.brouwerijdemolen.borefts2013.gui.screens.BeerActivity
import nl.brouwerijdemolen.borefts2013.gui.screens.BeerViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.BrewerActivity
import nl.brouwerijdemolen.borefts2013.gui.screens.BrewerViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.BrewersViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.StyleActivity
import nl.brouwerijdemolen.borefts2013.gui.screens.StyleViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.StylesViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val networkModule = applicationContext {
    bean { Api() }
    bean { Repository(get()) }
}

val uiModule = applicationContext {
    bean { ResourceProvider(get()) }
    bean { AppRater(get()) }
}

val viewModelsModule = applicationContext {
    viewModel { BrewersViewModel(get()) }
    viewModel { StylesViewModel(get()) }
    viewModel { params -> BrewerViewModel(params[BrewerActivity.KEY_BREWER], get()) }
    viewModel { params -> StyleViewModel(params[StyleActivity.KEY_STYLE], get()) }
    viewModel { params -> BeerViewModel(params[BeerActivity.KEY_BEER]) }
}
