package nl.brouwerijdemolen.borefts2013.gui.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.getOrElse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.CoroutineScope.ui
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.Repository

class StyleViewModel(
        private val style: Style,
        private val navigator: Navigator,
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<StyleUiModel>()

    init {
        GlobalScope.launch(ui) {
            state.postValue(StyleUiModel(style, repository.styleBeers(style.id)
                    .map { it.sortedBy { it.name } }
                    .getOrElse { throw IllegalStateException("StyleViewModel can only be created with cached data") }))
        }
    }

    fun openBeer(beer: Beer) {
        navigator.openBeer(beer)
    }

}

data class StyleUiModel(
        val style: Style,
        val beers: List<Beer>)
