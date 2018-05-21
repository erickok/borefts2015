package nl.brouwerijdemolen.borefts2013.gui

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.koin.log.EmptyLogger

class BoreftsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(networkModule, uiModule, viewModelsModule), logger = EmptyLogger())
    }

}
