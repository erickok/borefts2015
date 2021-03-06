package nl.brouwerijdemolen.borefts2013.gui.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import arrow.core.extensions.`try`.monad.binding
import arrow.core.fix
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.brouwerijdemolen.borefts2013.api.Area
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.Poi
import nl.brouwerijdemolen.borefts2013.gui.CoroutineScope.ui
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.Repository
import nl.brouwerijdemolen.borefts2013.gui.components.log

class InfoViewModel(
        private val navigator: Navigator,
        private val repository: Repository) : ViewModel() {

    val state = MutableLiveData<InfoUiModel>().apply { value = InfoUiModel.Loading }

    init {
        GlobalScope.launch(ui) {
            val tryBrewers = repository.brewers()
            val tryAreas = repository.areas()
            val tryPois = repository.pois()
            state.postValue(binding {
                val brewers = tryBrewers.bind()
                val areas = tryAreas.bind()
                val pois = tryPois.bind()
                InfoUiModel.Success(brewers, areas, pois)
            }.fix().toUiModel())
        }
    }

    private fun Try<InfoUiModel>.toUiModel(): InfoUiModel {
        return when (this) {
            is Success -> value
            is Failure -> InfoUiModel.Failure.also { this.log() }
        }
    }

    fun openMap() {
        navigator.openMap()
    }

    fun openMap(poiId: String) {
        navigator.openMapForPoi(poiId)
    }

}

sealed class InfoUiModel {
    object Loading : InfoUiModel()
    object Failure : InfoUiModel()
    data class Success(
            val brewers: List<Brewer>,
            val areas: List<Area>,
            val pois: List<Poi>) : InfoUiModel()
}
