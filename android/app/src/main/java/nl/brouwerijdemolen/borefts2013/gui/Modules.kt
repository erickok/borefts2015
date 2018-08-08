package nl.brouwerijdemolen.borefts2013.gui

import android.app.Activity
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
import org.koin.core.parameter.parametersOf
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
    factory { (activity: Activity) -> IntentNavigator(activity) as Navigator }
}

val viewModelsModule = module {
    viewModel { (activity: Activity) -> InfoViewModel(get(parameters = { parametersOf(activity) }), get()) }
    viewModel { (activity: Activity) -> BrewersViewModel(get(parameters = { parametersOf(activity) }), get()) }
    viewModel { (activity: Activity) -> StylesViewModel(get(parameters = { parametersOf(activity) }), get()) }
    viewModel { (activity: Activity) -> StarsViewModel(get(parameters = { parametersOf(activity) }), get(), get()) }
    viewModel { (activity: Activity, brewer: Brewer) -> BrewerViewModel(brewer, get(parameters = { parametersOf(activity) }), get()) }
    viewModel { (activity: Activity, style: Style) -> StyleViewModel(style, get(parameters = { parametersOf(activity) }), get()) }
    viewModel { (activity: Activity, beer: Beer) -> BeerViewModel(beer, get(parameters = { parametersOf(activity) }), get()) }
    viewModel { (activity: Activity, args: Args) -> MapViewModel(get(parameters = { parametersOf(activity) }), get(), args) }
}
