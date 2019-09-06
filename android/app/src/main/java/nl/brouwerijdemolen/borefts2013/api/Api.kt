package nl.brouwerijdemolen.borefts2013.api

import arrow.core.Try
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface Api {

    suspend fun pois(): Try<Pois>

    suspend fun brewers(): Try<Brewers>

    suspend fun styles(): Try<Styles>

    suspend fun beersRaw(): Try<Beers>

}

class HttpApi(private val okHttpClient: OkHttpClient) : Api {

    private val retrofit by lazy {
        Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://borefts-staging.firebaseio.com/") // d454d or staging
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private val routes by lazy { retrofit.create(Routes::class.java) }

    override suspend fun pois() = Try { routes.pois() }.nonNullBody()

    override suspend fun brewers() = Try { routes.brewers() }.nonNullBody()

    override suspend fun styles() = Try { routes.styles() }.nonNullBody()

    override suspend fun beersRaw() = Try { routes.beers() }.nonNullBody()

    private fun <T> Try<Response<T>>.nonNullBody() = this.filter { it.body() != null }.map { it.body()!! }

    interface Routes {

        @GET("https://2312.nl/borefts2018/pois.php")
        suspend fun pois(): Response<Pois>

        @GET("brewers/2019.json")
        suspend fun brewers(): Response<Brewers>

        @GET("styles/2019.json")
        suspend fun styles(): Response<Styles>

        @GET("beers/2019.json")
        suspend fun beers(): Response<Beers>

    }

}
