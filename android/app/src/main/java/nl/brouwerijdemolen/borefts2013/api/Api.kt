package nl.brouwerijdemolen.borefts2013.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Response
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

    suspend fun pois() = routes.pois().await()

    suspend fun brewers() = routes.brewers().await()

    suspend fun styles() = routes.styles().await()

    suspend fun beers() = routes.beers().await()

    interface Routes {

        @GET("http://2312.nl/borefts2017/pois.php")
        fun pois(): Deferred<Response<Pois>>

        @GET("brewers/2017.json")
        fun brewers(): Deferred<Response<Brewers>>

        @GET("styles/2017.json")
        fun styles(): Deferred<Response<Styles>>

        @GET("beers/2017.json")
        fun beers(): Deferred<Response<Beers>>

    }

}
