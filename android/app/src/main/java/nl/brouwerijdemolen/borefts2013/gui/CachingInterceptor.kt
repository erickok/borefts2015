package nl.brouwerijdemolen.borefts2013.gui

import nl.brouwerijdemolen.borefts2013.ext.div
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.buffer
import okio.sink
import okio.source
import java.io.File

class CachingInterceptor(private val cacheDir: File, private val maxAgeInMillis: Long) : Interceptor {

    private val mediaTypeJson = "application/json; charset=utf-8".toMediaTypeOrNull()
    private val cacheNameDisallowCharacters = "[^a-zA-Z0-9.\\-]".toRegex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val cacheName = chain.request().url.cacheName()
        val cacheFile = cacheDir / cacheName

        if (!cacheFile.exists() || cacheFile.lastModified() < (System.currentTimeMillis() - maxAgeInMillis)) {
            // Nothing cached or old
            return performAndCache(cacheFile, chain)
        }

        // Fresh enough cache file available
        return responseFromCache(cacheFile, chain.request())
    }

    private fun responseFromCache(cacheFile: File, chain: Request): Response {
        val cacheSource = cacheFile.source().buffer()
        return Response.Builder()
                .request(chain)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("Cached")
                .body(cacheSource.asResponseBody(mediaTypeJson, -1))
                .build()
    }

    private fun performAndCache(cacheFile: File, chain: Interceptor.Chain): Response {
        val request = chain.request()
        val freshResponse = chain.proceed(request)
        val responseBody = freshResponse.body
        if (!freshResponse.isSuccessful || responseBody == null) {
            // Failed request: use (old) cache if available, or return the error
            return if (cacheFile.exists()) responseFromCache(cacheFile, request) else freshResponse
        }

        // Data received: cache it
        val cacheSink = cacheFile.sink().buffer()
        cacheSink.writeAll(responseBody.source())
        cacheSink.close()

        return responseFromCache(cacheFile, request)
    }

    private fun HttpUrl.cacheName(): String {
        return this.toString().replace(cacheNameDisallowCharacters, "_")
    }

}
