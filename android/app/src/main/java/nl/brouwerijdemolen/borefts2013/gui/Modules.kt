package nl.brouwerijdemolen.borefts2013.gui

import android.content.Context
import nl.brouwerijdemolen.borefts2013.api.Api
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.components.AppRater
import nl.brouwerijdemolen.borefts2013.gui.components.ResourceProvider
import nl.brouwerijdemolen.borefts2013.gui.components.StarPersistence
import nl.brouwerijdemolen.borefts2013.gui.screens.Args
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
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

private const val CACHE_TIME_MEMORY = 300_000L // Five minutes
private const val CACHE_TIME_DISK = 1_800_000L // Half hour

val networkModule = module {
    single {
        OkHttpClient.Builder()
                .addInterceptor(CachingInterceptor(get<Context>().filesDir, CACHE_TIME_DISK))
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build()
    }
    single { Api(get()) }
    single { MemoryCache(CACHE_TIME_MEMORY) }
    single { Repository(get(), get()) }
}

val uiModule = module {
    single { ResourceProvider(get()) }
    single { AppRater(get()) }
    single { StarPersistence(get()) }
    single { IntentNavigator(get()) as Navigator }
}

val viewModelsModule = module {
    viewModel { InfoViewModel(get(), get()) }
    viewModel { BrewersViewModel(get(), get()) }
    viewModel { StylesViewModel(get(), get()) }
    viewModel { StarsViewModel(get(), get(), get()) }
    viewModel { (brewer: Brewer) -> BrewerViewModel(brewer, get(), get()) }
    viewModel { (style: Style) -> StyleViewModel(style, get(), get()) }
    viewModel { (beer: Beer) -> BeerViewModel(beer, get(), get()) }
    viewModel { (args: Args) -> MapViewModel(get(), get(), args) }
}
