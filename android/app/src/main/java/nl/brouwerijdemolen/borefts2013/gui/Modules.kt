package nl.brouwerijdemolen.borefts2013.gui

import nl.brouwerijdemolen.borefts2013.api.Api
import nl.brouwerijdemolen.borefts2013.ext.KEY_ARGS
import nl.brouwerijdemolen.borefts2013.gui.components.AppRater
import nl.brouwerijdemolen.borefts2013.gui.components.ResourceProvider
import nl.brouwerijdemolen.borefts2013.gui.components.StarPersistence
import nl.brouwerijdemolen.borefts2013.gui.screens.*
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val networkModule = applicationContext {
    bean { Api() }
    bean { Repository(get()) }
}

val uiModule = applicationContext {
    bean { ResourceProvider(get()) }
    bean { AppRater(get()) }
    bean { StarPersistence(get()) }
}

val viewModelsModule = applicationContext {
    viewModel { InfoViewModel(get()) }
    viewModel { BrewersViewModel(get()) }
    viewModel { StylesViewModel(get()) }
    viewModel { StarsViewModel(get(), get()) }
    viewModel { params -> BrewerViewModel(params[KEY_ARGS], get()) }
    viewModel { params -> StyleViewModel(params[KEY_ARGS], get()) }
    viewModel { params -> BeerViewModel(params[KEY_ARGS], get()) }
    viewModel { params -> MapViewModel(get(), params[KEY_ARGS]) }
}
