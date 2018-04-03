package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import arrow.data.getOrElse
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.Repository

class StyleViewModel(
        private val style: Style,
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<StyleUiModel>()

    init {
        launch(UI) {
            state.postValue(StyleUiModel(style, repository.styleBeers(style.id)
                    .getOrElse { throw IllegalStateException("StyleViewModel can only be created with cached data") }))
        }
    }

}

data class StyleUiModel(
        val style: Style,
        val beers: List<Beer>)
