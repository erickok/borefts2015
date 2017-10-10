package nl.brouwerijdemolen.borefts2013.gui.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class InfoController : Controller() {

    override fun onCreateView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
        return parent.verticalLayout {
            textView("Welcome!") {
                horizontalPadding = dip(16)
            }
        }
    }

}