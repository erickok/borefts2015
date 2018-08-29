package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import kotlinx.coroutines.experimental.launch
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.CoroutineScope.ui
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.Repository
import nl.brouwerijdemolen.borefts2013.gui.components.log

class StylesViewModel(
        private val navigator: Navigator,
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<StylesUiModel>().apply { value = StylesUiModel.Loading }

    init {
        launch(ui) {
            state.postValue(repository.styles().toUiModel())
        }
    }

    private fun Try<List<Style>>.toUiModel(): StylesUiModel {
        return when (this) {
            is Success -> StylesUiModel.Success(value)
            is Failure -> StylesUiModel.Failure.also { this.log() }
        }
    }

    fun openStyle(style: Style) {
        navigator.openStyle(style)
    }

    fun retry() {
        state.postValue(StylesUiModel.Loading)
        launch(ui) {
            state.postValue(repository.styles().toUiModel())
        }
    }

}

sealed class StylesUiModel {
    object Loading : StylesUiModel()
    object Failure : StylesUiModel()
    data class Success(val styles: List<Style>) : StylesUiModel()
}
