package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.gui.components.StarPersistence

class BeerViewModel(
        private val beer: Beer,
        private val starPersistence: StarPersistence) : ViewModel() {

    val state = MutableLiveData<BeerUiModel>().apply {
        value = BeerUiModel(beer, starPersistence.isStarred(beer.id))
    }

    fun updateStar(starred: Boolean) {
        val id = state.value!!.beer.id
        if (starred) {
            starPersistence.addStar(id)
        } else {
            starPersistence.removeStar(id)
        }
        state.value = BeerUiModel(state.value!!.beer, starred)
    }

}

data class BeerUiModel(
        val beer: Beer,
        val isStarred: Boolean)
