package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.gui.Repository
import nl.brouwerijdemolen.borefts2013.gui.Result

class BrewersViewModel(
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<BrewersUiModel>().apply { value = BrewersUiModel.Loading }

    init {
        launch(UI) {
            state.postValue(repository.brewers().toUiModel())
        }
    }

    private fun Result<List<Brewer>>.toUiModel(): BrewersUiModel {
        return when (this) {
            is Result.Data -> BrewersUiModel.Success(value)
            is Result.Error -> BrewersUiModel.Failure(message)
        }
    }

}

sealed class BrewersUiModel {
    object Loading : BrewersUiModel()
    data class Failure(val error: String?) : BrewersUiModel()
    data class Success(val brewers: List<Brewer>) : BrewersUiModel()
}
