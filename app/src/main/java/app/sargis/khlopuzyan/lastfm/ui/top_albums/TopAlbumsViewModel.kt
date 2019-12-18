package app.sargis.khlopuzyan.lastfm.ui.top_albums

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.sargis.khlopuzyan.lastfm.helper.SingleLiveEvent
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.model.top_albums.Attr
import app.sargis.khlopuzyan.lastfm.model.top_albums.Topalbums
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.repository.TopAlbumsRepository
import app.sargis.khlopuzyan.lastfm.util.AlbumCacheState
import app.sargis.khlopuzyan.lastfm.util.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopAlbumsViewModel constructor(
    private val topAlbumsRepository: TopAlbumsRepository
) : ViewModel() {

    private var availablePages: Int = 0
    private var loadedPageIndex: Int = 0
    var artistNameLiveData: MutableLiveData<String> = MutableLiveData()

    val openAlbumDetailLiveData: SingleLiveEvent<Album> = SingleLiveEvent()
    val showToastLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    val networkState = MutableLiveData<NetworkState>()

    var topAlbumsLiveData: MutableLiveData<MutableList<Album>> = MutableLiveData(mutableListOf())

    fun retry() {
        searchMoreAlbums()
    }

    fun onAlbumClick(album: Album) {
        viewModelScope.launch {

            when (val albumInfo = topAlbumsRepository.searchAlbumInfo(
                artist = album.artist?.name ?: "",
                album = album.name ?: ""
            )) {

                is Result.Success -> {
                    album.tracks = albumInfo.data.album?.tracks?.track
                    openAlbumDetailLiveData.value = album
                }

                is Result.Error -> {
                    showToastLiveData.value =
                        "Something went wrong!\nError code: ${albumInfo.errorCode}"
                }

                is Result.Failure -> {
                    showToastLiveData.value =
                        "Something went wrong!\nError message: ${albumInfo.error?.message}"
                }
            }
        }
    }

    // Caching

    fun onAlbumCacheActionClick(album: Album) {
        when (album.albumCacheState) {
            AlbumCacheState.NotCached -> {
                saveAlbumInCache(album)
            }
            AlbumCacheState.Cached -> {
                deleteAlbumFromCache(album)
            }
            else -> {
            }
        }
    }

    private fun saveAlbumInCache(album: Album) {

        viewModelScope.launch {

            val index: Int = topAlbumsLiveData.value?.indexOf(album)!!
            setAnimationState(album, index, AlbumCacheState.InProcess)

            when (val albumInfo = topAlbumsRepository.searchAlbumInfo(
                artist = album.artist?.name ?: "",
                album = album.name ?: ""
            )) {

                is Result.Success -> {
                    album.albumCacheState = AlbumCacheState.Cached
                    album.tracks = albumInfo.data.album?.tracks?.track
                    topAlbumsRepository.saveTopAlbumInCache(album)
                    album.albumCacheState = AlbumCacheState.InProcess
                    setAnimationState(album, index, AlbumCacheState.Cached)
                }

                is Result.Error -> {
                    showToastLiveData.value =
                        "Unable to save album!\nError code: ${albumInfo.errorCode}"
                    setAnimationState(album, index, AlbumCacheState.NotCached)
                }

                is Result.Failure -> {
                    showToastLiveData.value =
                        "Unable to save album!\nError message: ${albumInfo.error?.message}"
                    setAnimationState(album, index, AlbumCacheState.NotCached)
                }
            }
        }
    }

    private fun deleteAlbumFromCache(album: Album) {

        viewModelScope.launch {
            val index: Int = topAlbumsLiveData.value?.indexOf(album)!!
            setAnimationState(album, index, AlbumCacheState.InProcess)

            topAlbumsRepository.deleteTopAlbumFromCache(album)
            setAnimationState(album, index, AlbumCacheState.NotCached)
        }
    }

    private fun setAnimationState(album: Album, index: Int, albumCacheState: AlbumCacheState) {

        var newAlbum = album.copy(albumCacheState = albumCacheState)
        val newTopAlbums: MutableList<Album> = mutableListOf()

        newTopAlbums.addAll(topAlbumsLiveData.value!!)
        newTopAlbums[index] = newAlbum

        topAlbumsLiveData.value = newTopAlbums
    }


    fun setArtist(artistName: String?) {
        artistName?.let {
            artistNameLiveData.value = artistName
            topAlbumsLiveData.value = mutableListOf()
            loadedPageIndex = 0
            availablePages = 0
            searchMoreAlbums()
        }
    }

    fun searchMoreAlbums() {

        val artistName = artistNameLiveData.value ?: return

        viewModelScope.launch(Dispatchers.Main) {

            networkState.value = NetworkState.Loading

            when (val resultTopAlbums =
                topAlbumsRepository.searchTopAlbums(
                    page = (loadedPageIndex + 1).toString(),
                    artist = artistName
                )) {

                is Result.Success -> {

                    //TODO
                    syncSearchedTopAlbumsWithCached(
                        artistName,
                        resultTopAlbums.data.topalbums?.albums
                    )

                    networkState.value = NetworkState.Loaded
                    setPageInfo(resultTopAlbums.data.topalbums?.attr)
                    handleSearchResult(resultTopAlbums.data.topalbums)
                }

                is Result.Error -> {
                    showToastLiveData.value =
                        "Something went wrong!\nError code: ${resultTopAlbums.errorCode}"
                    networkState.value = NetworkState.Failure(null/*resultTopAlbums.errorCode*/)
                }

                is Result.Failure -> {
                    showToastLiveData.value =
                        "Something went wrong!\nError message: ${resultTopAlbums.error?.message}"
                    networkState.value = NetworkState.Failure(resultTopAlbums.error)
                }
            }
        }
    }

    private fun syncSearchedTopAlbumsWithCached(
        artistName: String,
        searchedTopAlbums: List<Album>?
    ) {

        searchedTopAlbums?.let {

            viewModelScope.launch {

                val allCachedTopAlbums =
                    topAlbumsRepository.getAllTopAlbumsFromCache() ?: return@launch

                val filteredCachedAlbums = allCachedTopAlbums.filter { cachedAlbum ->
                    cachedAlbum.artist?.name?.contains(artistName, ignoreCase = true) ?: false
                }

                for (searchedTopAlbum in searchedTopAlbums) {
                    for (cacheTopAlbum in filteredCachedAlbums) {
                        if (searchedTopAlbum.name == cacheTopAlbum.name) {
                            searchedTopAlbum.albumCacheState = AlbumCacheState.Cached
                            break
                        }
                    }
                }
            }
        }

    }

    private fun handleSearchResult(topalbums: Topalbums?) {

        var albums: MutableList<Album>?
        if (topalbums?.albums == null) {
            albums = mutableListOf()
        } else {
            albums = topAlbumsLiveData.value
            albums?.addAll(topalbums.albums)
        }

        topAlbumsLiveData.value = albums
    }


    private fun setPageInfo(attr: Attr?) {

        val page = attr?.page?.toInt() ?: 0
        val perPage = attr?.perPage?.toInt() ?: 50
        val totalPages = attr?.totalPages?.toInt() ?: 0

        this.loadedPageIndex = page

        var availablePages = totalPages / perPage
        //TODO Last.fm API Bug
        if (availablePages > 200) {
            availablePages = 200
        }
        this.availablePages = availablePages
    }

    fun hasExtraRow(): Boolean {
        return (networkState.value != null && networkState.value != NetworkState.Loaded) || (loadedPageIndex < availablePages)
    }

}