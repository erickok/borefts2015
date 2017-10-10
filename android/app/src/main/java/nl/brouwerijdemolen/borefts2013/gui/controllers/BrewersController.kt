package nl.brouwerijdemolen.borefts2013.gui.controllers

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.gui.components.LiveDataAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class BrewersController : LifecycleController() {

    private val brewersViewModel by lazy { BrewersViewModel() }

    override fun onCreateView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
        return parent.recyclerView {
            layoutManager = LinearLayoutManager(context)
            adapter = LiveDataAdapter(
                    this@BrewersController,
                    brewersViewModel.brewers,
                    { BrewerView(context) },
                    { v, value ->
                        run {
                            v.nameText.text = value.name
                            v.countryText.text = value.name
                        }
                    })
        }
    }

}

class BrewerView(context: Context) : LinearLayout(context) {

    lateinit var nameText: TextView
    lateinit var countryText: TextView

    init {
        addView(context.verticalLayout {
            horizontalPadding = dip(16)
            verticalPadding = dip(8)
            nameText = textView { textSize = 20f }
            countryText = textView { textSize = 15f }
        })
    }

}

class BrewersViewModel : ViewModel() {

    val brewers: LiveData<List<Brewer>> = MutableLiveData<List<Brewer>>().apply {
        value = listOf(
                Brewer(0, "logo", "Brouwerij De Molen", "De Molen", "Molen", "Bodegraven", "The Netherlands", latitude = 0f, longitude = 0f),
                Brewer(0, "logo", "Brasserie du Mont Salève", "Mont Salève", "Mont Saleve", "Neydens", "France", latitude = 0f, longitude = 0f))
    }

}