package nl.brouwerijdemolen.borefts2013.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class Api {

    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl("https://borefts-staging.firebaseio.com/") // d454d or staging
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private val routes by lazy { retrofit.create(Routes::class.java) }

    suspend fun pois(): Pois = routes.pois().await()

    suspend fun brewers(): Brewers = routes.brewers().await()

    suspend fun styles(): Styles = routes.styles().await()

    suspend fun beers(): Beers = routes.beers().await()

    interface Routes {

        @GET("http://2312.nl/borefts2017/pois.php")
        fun pois(): Deferred<Pois>

        @GET("brewers/2017.json")
        fun brewers(): Deferred<Brewers>

        @GET("styles/2017.json")
        fun styles(): Deferred<Styles>

        @GET("beers/2017.json")
        fun beers(): Deferred<Beers>

    }

}
