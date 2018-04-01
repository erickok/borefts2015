package nl.brouwerijdemolen.borefts2013.gui

import nl.brouwerijdemolen.borefts2013.api.Api
import nl.brouwerijdemolen.borefts2013.gui.components.AppRater
import nl.brouwerijdemolen.borefts2013.gui.components.Repository
import nl.brouwerijdemolen.borefts2013.gui.screens.BrewersViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.StylesViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val networkModule = applicationContext {
    bean { Api() }
    bean { Repository(get()) }
}

val uiModel = applicationContext {
    bean { AppRater(get()) }
    viewModel { BrewersViewModel(get()) }
    viewModel { StylesViewModel(get()) }
}