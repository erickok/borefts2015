package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import arrow.data.Failure
import arrow.data.Success
import arrow.data.Try
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.Repository
import nl.brouwerijdemolen.borefts2013.gui.components.log

class StylesViewModel(
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<StylesUiModel>().apply { value = StylesUiModel.Loading }

    init {
        launch(UI) {
            state.postValue(repository.styles().toUiModel())
        }
    }

    private fun Try<List<Style>>.toUiModel(): StylesUiModel {
        return when (this) {
            is Success -> StylesUiModel.Success(value)
            is Failure -> StylesUiModel.Failure.also { this.log() }
        }
    }

}

sealed class StylesUiModel {
    object Loading : StylesUiModel()
    object Failure : StylesUiModel()
    data class Success(val styles: List<Style>) : StylesUiModel()
}
