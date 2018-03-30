package nl.brouwerijdemolen.borefts2013.gui

import android.app.Application
import org.koin.android.ext.android.startKoin

class BoreftsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(networkModule, uiModel))
    }

}
