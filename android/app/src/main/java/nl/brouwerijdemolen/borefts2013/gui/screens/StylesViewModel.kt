package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.Repository
import nl.brouwerijdemolen.borefts2013.gui.Result

class StylesViewModel(
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<StylesUiModel>().apply { value = StylesUiModel.Loading }

    init {
        launch(UI) {
            state.postValue(repository.styles().toUiModel())
        }
    }

    private fun Result<List<Style>>.toUiModel(): StylesUiModel {
        return when (this) {
            is Result.Data -> StylesUiModel.Success(value)
            is Result.Error -> StylesUiModel.Failure(message)
        }
    }

}

sealed class StylesUiModel {
    object Loading : StylesUiModel()
    data class Failure(val error: String?) : StylesUiModel()
    data class Success(val styles: List<Style>) : StylesUiModel()
}
