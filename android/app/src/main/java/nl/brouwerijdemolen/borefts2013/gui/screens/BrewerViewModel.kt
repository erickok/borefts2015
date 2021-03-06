package nl.brouwerijdemolen.borefts2013.gui.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.getOrElse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.gui.CoroutineScope.ui
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.Repository

class BrewerViewModel(
        private val brewer: Brewer,
        private val navigator: Navigator,
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<BrewerUiModel>()

    init {
        GlobalScope.launch(ui) {
            state.postValue(BrewerUiModel(brewer, repository.brewerBeers(brewer.id)
                    .map { it.sortedBy { it.name } }
                    .getOrElse { throw IllegalStateException("BrewerViewModel can only be created with cached data", it) }))
        }
    }

    fun openBeer(beer: Beer) {
        navigator.openBeer(beer)
    }

}

data class BrewerUiModel(
        val brewer: Brewer,
        val beers: List<Beer>)
