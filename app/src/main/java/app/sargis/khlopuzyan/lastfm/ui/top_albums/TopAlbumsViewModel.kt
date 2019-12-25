package app.sargis.khlopuzyan.lastfm.ui.top_albums

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.sargis.khlopuzyan.lastfm.helper.SingleLiveEvent
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.model.top_albums.Attr
import app.sargis.khlopuzyan.lastfm.model.top_albums.TopAlbums
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.repository.TopAlbumsRepository
import app.sargis.khlopuzyan.lastfm.util.CachedState
import app.sargis.khlopuzyan.lastfm.util.DataLoadingState
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
    val dataLoadingStateLiveData = MutableLiveData<DataLoadingState>()

    var topAlbumsLiveData: MutableLiveData<MutableList<Album>> = MutableLiveData(mutableListOf())
    val errorMessageLiveData = MutableLiveData<String>()

    var cachedAlbumsLiveData = topAlbumsRepository.getAllCachedAlbumsLiveData().observeForever {
        val artistName = artistNameLiveData.value ?: return@observeForever
        it?.let {
            syncSearchedTopAlbumsWithCached(
                artistName,
                topAlbumsLiveData.value
            )
        }
    }

    /**
     * Handles retry icon click
     * */
    fun retryClick() {
        searchMoreAlbums()
    }

    /**
     * Handles album list item click
     * */
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
                        "Something went wrong.\nError code: ${albumInfo.errorCode}"
                }

                is Result.Failure -> {
                    showToastLiveData.value =
                        "Something went wrong.\nCheck your internet connection"
                }
            }
        }
    }

    // Caching

    /**
     * Handles caching icon click
     * */
    fun onCachingActionClick(album: Album) {
        when (album.cachedState) {
            CachedState.NotCached -> {
                saveAlbumInCache(album)
            }
            CachedState.Cached -> {
                deleteAlbumFromCache(album)
            }
            else -> {
            }
        }
    }

    /**
     * Saves album in cache
     *
     * @param album album to save in cache
     * */
    private fun saveAlbumInCache(album: Album) {

        viewModelScope.launch {

            val index: Int = topAlbumsLiveData.value?.indexOf(album)!!
            setAlbumCachingState(album, index, CachedState.InProcess)

            when (val albumInfo = topAlbumsRepository.searchAlbumInfo(
                artist = album.artist?.name ?: "",
                album = album.name ?: ""
            )) {

                is Result.Success -> {
                    album.cachedState = CachedState.Cached
                    album.tracks = albumInfo.data.album?.tracks?.track
                    topAlbumsRepository.saveTopAlbumInCache(album)
                    album.cachedState = CachedState.InProcess
                    setAlbumCachingState(album, index, CachedState.Cached)
                }

                is Result.Error -> {
                    showToastLiveData.value =
                        "Unable to save album.\nError code: ${albumInfo.errorCode}"
                    setAlbumCachingState(album, index, CachedState.NotCached)
                }

                is Result.Failure -> {
                    showToastLiveData.value =
                        "Unable to save album.\nError message: ${albumInfo.error?.message}"
                    setAlbumCachingState(album, index, CachedState.NotCached)
                }
            }
        }
    }

    /**
     * Deletes album from cache
     *
     * @param album album to delete from cache
     * */
    private fun deleteAlbumFromCache(album: Album) {

        viewModelScope.launch {
            val index: Int = topAlbumsLiveData.value?.indexOf(album)!!
            setAlbumCachingState(album, index, CachedState.InProcess)

            topAlbumsRepository.deleteTopAlbumFromCache(album)
            setAlbumCachingState(album, index, CachedState.NotCached)
        }
    }

    /**
     * Sets album caching state
     * Uses to show caching animation
     *
     * @param album album
     * @param index index in list
     * @param cachedState cached state
     * */
    private fun setAlbumCachingState(album: Album, index: Int, cachedState: CachedState) {

        var newAlbum = album.copy(cachedState = cachedState)
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

    /**
     * Loads api next page albums
     * */
    fun searchMoreAlbums() {

        val artistName = artistNameLiveData.value ?: return

        viewModelScope.launch(Dispatchers.Main) {

            dataLoadingStateLiveData.value = DataLoadingState.Loading

            when (val resultTopAlbums =
                topAlbumsRepository.searchTopAlbums(
                    page = (loadedPageIndex + 1).toString(),
                    artist = artistName
                )) {

                is Result.Success -> {

                    syncSearchedTopAlbumsWithCached(
                        artistName,
                        resultTopAlbums.data.topAlbums?.albums
                    )

                    dataLoadingStateLiveData.value = DataLoadingState.Loaded
                    setApiPageInfo(resultTopAlbums.data.topAlbums?.attr)
                    handleSearchResult(resultTopAlbums.data.topAlbums)
                }

                is Result.Error -> {
                    errorMessageLiveData.value =
                        "Something went wrong.\nError code: ${resultTopAlbums.errorCode}"
                    dataLoadingStateLiveData.value =
                        DataLoadingState.Failure(null/*resultTopAlbums.errorCode*/)
                }

                is Result.Failure -> {
                    errorMessageLiveData.value =
                        "Something went wrong.\nCheck your internet connection"
                    dataLoadingStateLiveData.value = DataLoadingState.Failure(resultTopAlbums.error)
                }
            }
        }
    }

    /**
     * Syncs searched top albums' albumCacheState with cached one
     * */
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
                        searchedTopAlbum.cachedState = CachedState.NotCached
                        if (searchedTopAlbum.name == cacheTopAlbum.name) {
                            searchedTopAlbum.cachedState = CachedState.Cached
                            break
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles search result
     * */
    private fun handleSearchResult(topAlbums: TopAlbums?) {

        var albums: MutableList<Album>?
        if (topAlbums?.albums == null) {
            albums = mutableListOf()
        } else {
            albums = topAlbumsLiveData.value
            albums?.addAll(topAlbums.albums)
        }

        topAlbumsLiveData.value = albums
    }

    /**
     * Sets api info (loaded page index, available pages, etc)
     *
     * @param attr attr returned from api call
     * */
    private fun setApiPageInfo(attr: Attr?) {

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

    /**
     * Checks weather api has pages available
     * */
    fun hasExtraRow(): Boolean {
        return (dataLoadingStateLiveData.value != null && dataLoadingStateLiveData.value != DataLoadingState.Loaded) || (loadedPageIndex < availablePages)
    }

}