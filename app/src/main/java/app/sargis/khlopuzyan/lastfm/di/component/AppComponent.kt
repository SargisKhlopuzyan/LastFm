package app.sargis.khlopuzyan.lastfm.di.component

import android.content.Context
import app.sargis.khlopuzyan.lastfm.LastFmApp
import app.sargis.khlopuzyan.lastfm.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        CachedAlbumsModule::class,
        AlbumDetailsModule::class,
        ArtistsSearchModule::class,
        TopAlbumsModule::class
    ]
)
interface AppComponent : AndroidInjector<LastFmApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Context): AppComponent
    }

}