package app.sargis.khlopuzyan.lastfm.ui.cachedAlbums

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.sargis.khlopuzyan.lastfm.helper.SingleLiveEvent
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.repository.CachedAlbumsRepository
import kotlinx.coroutines.launch

class CachedAlbumsViewModel constructor(
    private val cachedAlbumsRepository: CachedAlbumsRepository
) : ViewModel() {

    val openSearchLiveData: SingleLiveEvent<View> = SingleLiveEvent()
    val openAlbumDetailLiveData: SingleLiveEvent<Album> = SingleLiveEvent()

    var locallyStoredAlbumsLiveData: MutableLiveData<List<Album>> = MutableLiveData(listOf())

    init {
        loadLocallyStoredAlbums()
    }

    fun loadLocallyStoredAlbums() {
        viewModelScope.launch {
            val locallyStoredAlbums: List<Album>? = cachedAlbumsRepository.getAllAlbums()
            locallyStoredAlbums?.let {
                locallyStoredAlbumsLiveData.value = locallyStoredAlbums
            }
        }
    }

    fun onSearchClick(view: View) {
        openSearchLiveData.value = view
    }

    fun onAlbumClick(album: Album) {
        openAlbumDetailLiveData.value = album
    }

}