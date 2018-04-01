package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.gui.Repository
import nl.brouwerijdemolen.borefts2013.gui.Result

class BrewerViewModel(
        private val brewer: Brewer,
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<BrewerUiModel>()

    init {
        launch(UI) {
            state.postValue(BrewerUiModel(brewer.name, brewer.city, repository.brewerBeers(brewer.id).requireSuccess()))
        }
    }

    private fun Result<List<Beer>>.requireSuccess(): List<Beer> {
        return when (this) {
            is Result.Error -> throw IllegalStateException("BrewerViewModel can only be created with cached data")
            is Result.Data -> this.value
        }
    }

}

data class BrewerUiModel(
        val name: String,
        val location: String,
        val beers: List<Beer>)
