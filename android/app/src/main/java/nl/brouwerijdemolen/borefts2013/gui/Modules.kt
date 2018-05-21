package nl.brouwerijdemolen.borefts2013.gui

import android.content.Context
import nl.brouwerijdemolen.borefts2013.api.Api
import nl.brouwerijdemolen.borefts2013.ext.KEY_ARGS
import nl.brouwerijdemolen.borefts2013.gui.components.AppRater
import nl.brouwerijdemolen.borefts2013.gui.components.ResourceProvider
import nl.brouwerijdemolen.borefts2013.gui.components.StarPersistence
import nl.brouwerijdemolen.borefts2013.gui.screens.BeerViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.BrewerViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.BrewersViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.InfoViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.MapViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.StarsViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.StyleViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.StylesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

private const val ONE_HOUR = 3_600_000L

val networkModule = applicationContext {
    bean {
        OkHttpClient.Builder()
                .addInterceptor(CachingInterceptor(get<Context>().filesDir, ONE_HOUR))
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build()
    }
    bean { Api(get()) }
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
