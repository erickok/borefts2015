package nl.brouwerijdemolen.borefts2013.gui

import android.app.Activity
import android.os.Bundle
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import nl.brouwerijdemolen.borefts2013.gui.controllers.TabsController
import org.jetbrains.anko.frameLayout

class BoreftsActivity : Activity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = frameLayout().also {
            setContentView(it)
        }

        router = Conductor.attachRouter(this, container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(TabsController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

}