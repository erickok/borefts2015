package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import arrow.core.Try
import kotlinx.coroutines.experimental.launch
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.gui.CoroutineScope.ui
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.Repository
import nl.brouwerijdemolen.borefts2013.gui.components.StarPersistence
import nl.brouwerijdemolen.borefts2013.gui.components.log

class StarsViewModel(
        private val navigator: Navigator,
        private val repository: Repository,
        private val starPersistence: StarPersistence) : ViewModel() {

    val state = MutableLiveData<StarsUiModel>().apply { value = StarsUiModel.Loading }

    fun refresh() {
        launch(ui) {
            val starred = starPersistence.allStars
            state.postValue(repository.someBeers(starred).toUiModel())
        }
    }

    private fun Try<List<Beer>>.toUiModel(): StarsUiModel {
        return when (this) {
            is Try.Success -> if (value.isEmpty()) StarsUiModel.Empty else StarsUiModel.Success(value)
            is Try.Failure -> StarsUiModel.Failure.also { this.log() }
        }
    }

    fun openBeer(beer: Beer) {
        navigator.openBeer(beer)
    }

}

sealed class StarsUiModel {
    object Loading : StarsUiModel()
    object Failure : StarsUiModel()
    object Empty : StarsUiModel()
    data class Success(val beers: List<Beer>) : StarsUiModel()
}
