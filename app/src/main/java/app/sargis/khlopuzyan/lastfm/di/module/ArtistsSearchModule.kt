package app.sargis.khlopuzyan.lastfm.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.sargis.khlopuzyan.lastfm.di.annotation.ViewModelKey
import app.sargis.khlopuzyan.lastfm.repository.ArtistsSearchRepository
import app.sargis.khlopuzyan.lastfm.ui.artists_search.ArtistsSearchFragment
import app.sargis.khlopuzyan.lastfm.ui.artists_search.ArtistsSearchViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
@Module(includes = [ArtistsSearchModule.ProvideViewModel::class])
interface ArtistsSearchModule {

    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    fun bind(): ArtistsSearchFragment

    @Module
    class ProvideViewModel {
        @Provides
        @IntoMap
        @ViewModelKey(ArtistsSearchViewModel::class)
        fun provideSearchViewModel(
            searchRepository: ArtistsSearchRepository
        ): ViewModel = ArtistsSearchViewModel(searchRepository)
    }

    @Module
    class InjectViewModel {
        @Provides
        fun provideSearchViewModel(
            factory: ViewModelProvider.Factory,
            target: ArtistsSearchFragment
        ) = ViewModelProvider(target, factory)[ArtistsSearchViewModel::class.java]
    }

}