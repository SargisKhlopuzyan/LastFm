package app.sargis.khlopuzyan.lastfm.ui.cached_albums

import android.view.View
import androidx.lifecycle.ViewModel
import app.sargis.khlopuzyan.lastfm.helper.SingleLiveEvent
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.repository.CachedAlbumsRepository

class CachedAlbumsViewModel constructor(
    cachedAlbumsRepository: CachedAlbumsRepository
) : ViewModel() {

    val openSearchLiveData: SingleLiveEvent<View> = SingleLiveEvent()
    val openAlbumDetailLiveData: SingleLiveEvent<Album> = SingleLiveEvent()

    var cachedAlbumsLiveData = cachedAlbumsRepository.getAllCachedAlbumsLiveData()

    /**
     * Handles search icon click
     * */
    fun onSearchClick(v: View) {
        openSearchLiveData.value = v
    }

    /**
     * Handles album list item click
     * */
    fun onAlbumClick(album: Album) {
        openAlbumDetailLiveData.value = album
    }

}