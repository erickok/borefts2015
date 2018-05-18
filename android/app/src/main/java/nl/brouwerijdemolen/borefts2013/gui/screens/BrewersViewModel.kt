package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.gui.Repository
import nl.brouwerijdemolen.borefts2013.gui.components.log

class BrewersViewModel(
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<BrewersUiModel>().apply { value = BrewersUiModel.Loading }

    init {
        launch(UI) {
            state.postValue(repository.brewers().toUiModel())
        }
    }

    private fun Try<List<Brewer>>.toUiModel(): BrewersUiModel {
        return when (this) {
            is Success -> BrewersUiModel.Success(value)
            is Failure -> BrewersUiModel.Failure.also { this.log() }
        }
    }

}

sealed class BrewersUiModel {
    object Loading : BrewersUiModel()
    object Failure : BrewersUiModel()
    data class Success(val brewers: List<Brewer>) : BrewersUiModel()
}
