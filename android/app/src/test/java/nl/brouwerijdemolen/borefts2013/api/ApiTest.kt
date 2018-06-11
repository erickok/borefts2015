package nl.brouwerijdemolen.borefts2013.api

import arrow.core.Try
import kotlinx.coroutines.experimental.runBlocking
import nl.brouwerijdemolen.borefts2013.test.isSuccess
import okhttp3.OkHttpClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ApiTest {

    private val okHttpClient = OkHttpClient()
    private val api = Api(okHttpClient)

    @Test
    fun pois() {
        val pois = runBlocking { api.pois() }

        assertThat(pois).isSuccess()
        val value = (pois as Try.Success).value
        assertThat(value.pois).isNotEmpty()
        assertThat(value.areas).isNotEmpty()
    }

    @Test
    fun brewers() {
        val brewers = runBlocking { api.brewers() }

        assertThat(brewers).isSuccess()
        val value = (brewers as Try.Success).value
        assertThat(value.brewers).isNotEmpty()
    }

    @Test
    fun styles() {
        val brewers = runBlocking { api.styles() }

        assertThat(brewers).isSuccess()
        val value = (brewers as Try.Success).value
        assertThat(value.styles).isNotEmpty()
    }

    @Test
    fun beers() {
        val brewers = runBlocking { api.beersRaw() }

        assertThat(brewers).isSuccess()
        val value = (brewers as Try.Success).value
        assertThat(value.beers).isNotEmpty()
        assertThat(value.beers[0].brewerId).isGreaterThan(0)
        assertThat(value.beers[0].styleId).isGreaterThan(0)
    }

}
