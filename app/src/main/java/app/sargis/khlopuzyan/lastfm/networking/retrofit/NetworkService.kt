package app.sargis.khlopuzyan.lastfm.networking.retrofit

import app.sargis.khlopuzyan.lastfm.BuildConfig
import app.sargis.khlopuzyan.lastfm.networking.interceptor.AddApiKeyInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
class NetworkService {

    companion object {

        private const val BASE_URL = "http://ws.audioscrobbler.com/2.0/"

        fun initOkHttpClient(addApiKeyInterceptor: AddApiKeyInterceptor): OkHttpClient {
            return OkHttpClient.Builder().apply {
                readTimeout(60, TimeUnit.SECONDS)
                connectTimeout(60, TimeUnit.SECONDS)
                addInterceptor(addApiKeyInterceptor)

                // interceptor for logging
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BASIC
                    addInterceptor(logging)
                }
            }.build()
        }

        fun initRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder()
                            .build()
                    )
                )
                .build()
    }

}