package app.sargis.khlopuzyan.lastfm.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.sargis.khlopuzyan.lastfm.di.annotation.ViewModelKey
import app.sargis.khlopuzyan.lastfm.repository.CachedAlbumsRepository
import app.sargis.khlopuzyan.lastfm.ui.cached_albums.CachedAlbumsFragment
import app.sargis.khlopuzyan.lastfm.ui.cached_albums.CachedAlbumsViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
@Module(includes = [CachedAlbumsModule.ProvideViewModel::class])
interface CachedAlbumsModule {

    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    fun bind(): CachedAlbumsFragment

    @Module
    class ProvideViewModel {
        @Provides
        @IntoMap
        @ViewModelKey(CachedAlbumsViewModel::class)
        fun provideMainViewModel(
            cachedAlbumsRepository: CachedAlbumsRepository
        ): ViewModel = CachedAlbumsViewModel(cachedAlbumsRepository)
    }

    @Module
    class InjectViewModel {
        @Provides
        fun provideCachedAlbumsViewModel(
            factory: ViewModelProvider.Factory,
            target: CachedAlbumsFragment
        ) = ViewModelProvider(target, factory)[CachedAlbumsViewModel::class.java]
    }

}