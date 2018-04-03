package nl.brouwerijdemolen.borefts2013.api

import arrow.data.Try
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class Api {

    private val retrofit by lazy {
        Retrofit.Builder()
                .client(OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                        .build())
                .baseUrl("https://borefts-staging.firebaseio.com/") // d454d or staging
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private val routes by lazy { retrofit.create(Routes::class.java) }

    suspend fun pois() = Try { routes.pois().await() }.nonNullBody()

    suspend fun brewers() = Try { routes.brewers().await() }.nonNullBody()

    suspend fun styles() = Try { routes.styles().await() }.nonNullBody()

    suspend fun beersRaw() = Try { routes.beers().await() }.nonNullBody()

    private fun <T> Try<Response<T>>.nonNullBody() = this.filter { it.body() != null }.map { it.body()!! }

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
