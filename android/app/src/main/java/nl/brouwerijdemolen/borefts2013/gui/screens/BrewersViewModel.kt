package nl.brouwerijdemolen.borefts2013.gui.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.gui.CoroutineScope.ui
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.Repository
import nl.brouwerijdemolen.borefts2013.gui.components.log
import nl.brouwerijdemolen.borefts2013.gui.sortFilter

class BrewersViewModel(
        private val navigator: Navigator,
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<BrewersUiModel>().apply { value = BrewersUiModel.Loading }

    init {
        GlobalScope.launch(ui) {
            state.postValue(repository.brewers().toUiModel())
        }
    }

    private fun Try<List<Brewer>>.toUiModel(): BrewersUiModel {
        return when (this) {
            is Success -> BrewersUiModel.Success(value.sortedBy { it.sortFilter })
            is Failure -> BrewersUiModel.Failure.also { this.log() }
        }
    }

    fun openBrewer(brewer: Brewer) {
        navigator.openBrewer(brewer)
    }

    fun retry() {
        state.postValue(BrewersUiModel.Loading)
        GlobalScope.launch(ui) {
            state.postValue(repository.brewers().toUiModel())
        }
    }

}

sealed class BrewersUiModel {
    object Loading : BrewersUiModel()
    object Failure : BrewersUiModel()
    data class Success(val brewers: List<Brewer>) : BrewersUiModel()
}
