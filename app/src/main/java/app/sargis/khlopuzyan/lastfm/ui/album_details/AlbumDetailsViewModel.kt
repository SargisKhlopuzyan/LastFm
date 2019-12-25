package app.sargis.khlopuzyan.lastfm.ui.album_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.repository.AlbumDetailsRepository
import app.sargis.khlopuzyan.lastfm.util.CachedState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumDetailsViewModel constructor(
    private val albumDetailsRepository: AlbumDetailsRepository
) : ViewModel() {

    var albumLiveData = MutableLiveData<Album>()

    fun setAlbum(album: Album?) {
        viewModelScope.launch(Dispatchers.Main) {
            album?.name?.let {
                albumLiveData.value = album
            }
        }
    }

    /**
     * Handles caching icon click
     * */
    fun onCachingActionClick() {
        albumLiveData.value?.let {
            when (albumLiveData.value!!.cachedState) {
                CachedState.NotCached -> {
                    saveAlbumInCache()
                }
                CachedState.Cached -> {
                    deleteAlbumFromCache()
                }
                else -> {
                }
            }
        }
    }

    /**
     * Deletes album from cache
     * */
    private fun deleteAlbumFromCache() {
        albumLiveData.value?.let {
            viewModelScope.launch {
                setAlbumCachingState(CachedState.InProcess)
                albumLiveData.value!!.cachedState = CachedState.NotCached
                albumDetailsRepository.deleteTopAlbumFromCache(albumLiveData.value!!)
                setAlbumCachingState(CachedState.NotCached)
            }
        }
    }

    /**
     * Saves album in cache
     * */
    private fun saveAlbumInCache() {
        albumLiveData.value?.let {
            viewModelScope.launch {
                setAlbumCachingState(CachedState.InProcess)
                albumLiveData.value!!.cachedState = CachedState.Cached
                albumDetailsRepository.saveTopAlbumInCache(albumLiveData.value!!)
                setAlbumCachingState(CachedState.Cached)
            }
        }
    }

    /**
     * Sets album caching state
     * Uses to show caching animation
     *
     * @param cachedState cached state
     * */
    private fun setAlbumCachingState(cachedState: CachedState) {
        albumLiveData.value?.let {
            var newAlbum = albumLiveData.value!!.copy(cachedState = cachedState)
            albumLiveData.value = newAlbum
        }
    }

}
