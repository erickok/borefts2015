package nl.brouwerijdemolen.borefts2013.gui

import android.content.Context
import kotlinx.coroutines.experimental.android.UI
import nl.brouwerijdemolen.borefts2013.api.Api
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.HttpApi
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.components.AppRater
import nl.brouwerijdemolen.borefts2013.gui.components.ResourceProvider
import nl.brouwerijdemolen.borefts2013.gui.components.StarPersistence
import nl.brouwerijdemolen.borefts2013.gui.screens.BeerViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.BrewerViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.BrewersViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.InfoViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.MapViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.MapViewModelArgs
import nl.brouwerijdemolen.borefts2013.gui.screens.StarsViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.StyleViewModel
import nl.brouwerijdemolen.borefts2013.gui.screens.StylesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import kotlin.coroutines.experimental.CoroutineContext

private const val CACHE_TIME_MEMORY = 1_800_000L // Half hour
private const val CACHE_TIME_DISK = 1_800_000L // Half hour

val networkModule = module {
    single {
        OkHttpClient.Builder()
                .addInterceptor(CachingInterceptor(get<Context>().filesDir, CACHE_TIME_DISK))
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build()
    }
    single { HttpApi(get()) as Api }
    single { MemoryCache(CACHE_TIME_MEMORY) }
    single { Repository(get(), get()) }
}

val uiModule = module {
    single("ui") { UI as CoroutineContext }
    single { ResourceProvider(get()) }
    single { AppRater(get()) }
    single { StarPersistence(get()) }
    factory { IntentNavigator(get()) as Navigator }
}

val viewModelsModule = module {
    viewModel { InfoViewModel(get(), get()) }
    viewModel { BrewersViewModel(get(), get()) }
    viewModel { StylesViewModel(get(), get()) }
    viewModel { StarsViewModel(get(), get(), get()) }
    viewModel { (brewer: Brewer) -> BrewerViewModel(brewer, get(), get()) }
    viewModel { (style: Style) -> StyleViewModel(style, get(), get()) }
    viewModel { (beer: Beer) -> BeerViewModel(beer, get(), get()) }
    viewModel { (args: MapViewModelArgs) -> MapViewModel(get(), get(), args) }
}
