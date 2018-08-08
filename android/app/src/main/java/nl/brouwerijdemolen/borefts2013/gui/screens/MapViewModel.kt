package nl.brouwerijdemolen.borefts2013.gui.screens

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Parcelable
import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import arrow.core.fix
import arrow.core.monad
import arrow.typeclasses.binding
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.experimental.launch
import nl.brouwerijdemolen.borefts2013.api.Area
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.Poi
import nl.brouwerijdemolen.borefts2013.gui.CoroutineScope.ui
import nl.brouwerijdemolen.borefts2013.gui.Navigator
import nl.brouwerijdemolen.borefts2013.gui.Repository
import nl.brouwerijdemolen.borefts2013.gui.components.log

class MapViewModel(
        private val navigator: Navigator,
        private val repository: Repository,
        private val args: Args) : ViewModel() {

    val state = MutableLiveData<MapUiModel>().apply { value = MapUiModel.Loading }

    init {
        launch(ui) {
            val tryBrewers = repository.brewers()
            val tryAreas = repository.areas()
            val tryPois = repository.pois()
            state.postValue(Try.monad().binding {
                val brewers = tryBrewers.bind()
                val areas = tryAreas.bind()
                val pois = tryPois.bind()
                MapUiModel.Success(brewers, areas, pois, args.focusBrewerId, args.focusPoiId)
            }.fix().toUiModel())
        }
    }

    private fun Try<MapUiModel>.toUiModel(): MapUiModel {
        return when (this) {
            is Success -> value
            is Failure -> MapUiModel.Failure.also { this.log() }
        }
    }

    fun openBrewer(brewer: Brewer) {
        navigator.openBrewer(brewer)
    }

}

@Parcelize
data class Args(val focusBrewerId: Int? = null, val focusPoiId: String? = null) : Parcelable

sealed class MapUiModel {
    object Loading : MapUiModel()
    object Failure : MapUiModel()
    data class Success(
            val brewers: List<Brewer>,
            val areas: List<Area>,
            val pois: List<Poi>,
            val focusBrewerId: Int?,
            val focusPoiId: String?) : MapUiModel()
}
