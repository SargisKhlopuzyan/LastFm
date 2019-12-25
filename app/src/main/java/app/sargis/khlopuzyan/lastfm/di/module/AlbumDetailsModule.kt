package app.sargis.khlopuzyan.lastfm.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.sargis.khlopuzyan.lastfm.di.annotation.ViewModelKey
import app.sargis.khlopuzyan.lastfm.repository.AlbumDetailsRepository
import app.sargis.khlopuzyan.lastfm.ui.album_details.AlbumDetailsFragment
import app.sargis.khlopuzyan.lastfm.ui.album_details.AlbumDetailsViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
@Module(includes = [AlbumDetailsModule.ProvideViewModel::class])
interface AlbumDetailsModule {

    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    fun bind(): AlbumDetailsFragment

    @Module
    class ProvideViewModel {
        @Provides
        @IntoMap
        @ViewModelKey(AlbumDetailsViewModel::class)
        fun provideDetailViewModel(
            albumDetailsRepository: AlbumDetailsRepository
        ): ViewModel = AlbumDetailsViewModel(albumDetailsRepository)
    }

    @Module
    class InjectViewModel {
        @Provides
        fun provideDetailViewModel(
            factory: ViewModelProvider.Factory,
            target: AlbumDetailsFragment
        ) = ViewModelProvider(target, factory)[AlbumDetailsViewModel::class.java]
    }

}