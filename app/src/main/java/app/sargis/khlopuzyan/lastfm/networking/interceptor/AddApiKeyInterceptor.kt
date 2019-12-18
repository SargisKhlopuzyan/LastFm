package app.sargis.khlopuzyan.lastfm.networking.interceptor

import app.sargis.khlopuzyan.lastfm.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
class AddApiKeyInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        val httpUrl = original.url
        val newUrl = httpUrl.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
        val newRequest = original.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        private const val apiKey = BuildConfig.api_key
    }

}