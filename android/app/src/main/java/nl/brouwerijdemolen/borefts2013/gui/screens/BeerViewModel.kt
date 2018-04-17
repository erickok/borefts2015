package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import nl.brouwerijdemolen.borefts2013.api.Beer

class BeerViewModel(private val beer: Beer) : ViewModel() {

    val state = MutableLiveData<BeerUiModel>().apply { BeerUiModel(beer) }

}

data class BeerUiModel(val beer: Beer)
