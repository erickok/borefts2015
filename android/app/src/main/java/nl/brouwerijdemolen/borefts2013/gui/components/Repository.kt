package nl.brouwerijdemolen.borefts2013.gui.components

import nl.brouwerijdemolen.borefts2013.api.Api
import retrofit2.Response

// TODO Implement persistent caching
class Repository(private val api: Api) {

    suspend fun pois() = api.pois().toResult { it.pois }

    suspend fun areas() = api.pois().toResult { it.areas }

    suspend fun brewers() = api.brewers().toResult { it.brewers }

    suspend fun styles() = api.styles().toResult { it.styles }

    suspend fun brewerBeers(brewerId: Int) = api.beers().toResult { it.beers.filter { it.brewerId == brewerId } }

    suspend fun styleBeers(styleId: Int) = api.beers().toResult { it.beers.filter { it.styleId == styleId } }

    private fun <T, R> Response<T>.toResult(transformer: (T) -> R): Result<R> {
        val body = body()
        return if (isSuccessful && body != null) {
            Result.Data(transformer(body))
        } else {
            Result.Error(errorBody()?.string())
        }
    }
}

sealed class Result<T> {
    data class Data<T>(val value: T) : Result<T>()
    data class Error<T>(val message: String?) : Result<T>()
}
