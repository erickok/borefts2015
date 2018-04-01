package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.Repository
import nl.brouwerijdemolen.borefts2013.gui.Result

class StyleViewModel(
        private val style: Style,
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<StyleUiModel>()

    init {
        launch(UI) {
            state.postValue(StyleUiModel(style.name, style.color, repository.styleBeers(style.id).requireSuccess()))
        }
    }

    private fun Result<List<Beer>>.requireSuccess(): List<Beer> {
        return when (this) {
            is Result.Error -> throw IllegalStateException("StyleViewModel can only be created with cached data")
            is Result.Data -> this.value
        }
    }

}

data class StyleUiModel(
        val name: String,
        val color: Int,
        val beers: List<Beer>)
