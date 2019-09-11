package nl.brouwerijdemolen.borefts2013.gui

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BoreftsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BoreftsApplication)
            modules(listOf(networkModule, uiModule, viewModelsModule))
        }
    }

}
