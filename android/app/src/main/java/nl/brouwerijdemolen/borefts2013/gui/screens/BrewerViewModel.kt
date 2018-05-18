package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import arrow.core.getOrElse
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.gui.Repository

class BrewerViewModel(
        private val brewer: Brewer,
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<BrewerUiModel>()

    init {
        launch(UI) {
            state.postValue(BrewerUiModel(brewer, repository.brewerBeers(brewer.id)
                    .getOrElse { throw IllegalStateException("BrewerViewModel can only be created with cached data") }))
        }
    }

}

data class BrewerUiModel(
        val brewer: Brewer,
        val beers: List<Beer>)
