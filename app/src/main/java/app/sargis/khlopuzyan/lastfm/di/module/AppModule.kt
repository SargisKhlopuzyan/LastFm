package app.sargis.khlopuzyan.lastfm.di.module

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.sargis.khlopuzyan.lastfm.database.DatabaseManager
import app.sargis.khlopuzyan.lastfm.di.factory.AppViewModelFactory
import app.sargis.khlopuzyan.lastfm.networking.api.ApiService
import app.sargis.khlopuzyan.lastfm.networking.interceptor.AddApiKeyInterceptor
import app.sargis.khlopuzyan.lastfm.networking.retrofit.NetworkService
import app.sargis.khlopuzyan.lastfm.repository.*
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
@Module(includes = [AppModule.ProvideViewModel::class])
abstract class AppModule {

    @Module
    class ProvideViewModel {

        @Provides
        @Singleton
        fun provideExecutor(): Executor = Executors.newFixedThreadPool(2)

        @Provides
        @Singleton
        fun provideOkHttpClient(addApiKeyInterceptor: AddApiKeyInterceptor): OkHttpClient =
            NetworkService.initOkHttpClient(addApiKeyInterceptor)

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            NetworkService.initRetrofit(okHttpClient)

        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create()

        @Provides
        @Singleton
        fun provideDatabaseManager(
            context: Context
        ): DatabaseManager = DatabaseManager(context)

        @Provides
        fun provideViewModelFactory(
            providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory = AppViewModelFactory(providers)

        @Provides
        @Singleton
        fun provideCachedAlbumsRepository(
            databaseManager: DatabaseManager
        ): CachedAlbumsRepository =
            CachedAlbumsRepositoryImpl(
                databaseManager
            )

        @Provides
        @Singleton
        fun provideArtistsSearchRepository(
            apiService: ApiService
        ): ArtistsSearchRepository =
            ArtistsSearchRepositoryImpl(
                apiService,
                CoroutineScope(Job() + Dispatchers.IO)
            )

        @Provides
        @Singleton
        fun provideTopAlbumsRepository(
            apiService: ApiService,
            databaseManager: DatabaseManager
        ): TopAlbumsRepository =
            TopAlbumsRepositoryImpl(
                apiService,
                databaseManager,
                CoroutineScope(Job() + Dispatchers.IO)
            )

        @Provides
        @Singleton
        fun provideAlbumDetailsRepository(
            databaseManager: DatabaseManager
        ): AlbumDetailsRepository =
            AlbumDetailsRepositoryImpl(
                databaseManager
            )
    }

}