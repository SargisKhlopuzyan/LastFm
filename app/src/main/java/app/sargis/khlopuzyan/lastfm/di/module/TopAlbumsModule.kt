package app.sargis.khlopuzyan.lastfm.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.sargis.khlopuzyan.lastfm.di.annotation.ViewModelKey
import app.sargis.khlopuzyan.lastfm.repository.TopAlbumsRepository
import app.sargis.khlopuzyan.lastfm.ui.top_albums.TopAlbumsFragment
import app.sargis.khlopuzyan.lastfm.ui.top_albums.TopAlbumsViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
@Module(includes = [TopAlbumsModule.ProvideViewModel::class])
interface TopAlbumsModule {

    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    fun bind(): TopAlbumsFragment

    @Module
    class ProvideViewModel {
        @Provides
        @IntoMap
        @ViewModelKey(TopAlbumsViewModel::class)
        fun provideTopAlbumsViewModel(
            topAlbumsRepository: TopAlbumsRepository
        ): ViewModel = TopAlbumsViewModel(topAlbumsRepository)
    }

    @Module
    class InjectViewModel {
        @Provides
        fun provideTopAlbumsViewModel(
            factory: ViewModelProvider.Factory,
            target: TopAlbumsFragment
        ) = ViewModelProvider(target, factory)[TopAlbumsViewModel::class.java]
    }

}