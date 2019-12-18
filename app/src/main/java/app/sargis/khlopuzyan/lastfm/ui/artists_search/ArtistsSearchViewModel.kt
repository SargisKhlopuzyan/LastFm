package app.sargis.khlopuzyan.lastfm.ui.artists_search

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.sargis.khlopuzyan.lastfm.helper.SingleLiveEvent
import app.sargis.khlopuzyan.lastfm.model.artists_search.Artist
import app.sargis.khlopuzyan.lastfm.model.artists_search.Results
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.repository.ArtistsSearchRepository
import app.sargis.khlopuzyan.lastfm.util.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistsSearchViewModel constructor(
    private val searchRepository: ArtistsSearchRepository
) : ViewModel() {

    private var artistName: String? = null
    private var availablePages: Int = 0
    private var loadedPageIndex: Int = 0
    private var searchQuery: String = ""

    val openTopAlbumsLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    val networkState = MutableLiveData<NetworkState>()

    val artistsLiveData = MutableLiveData<MutableList<Artist>>(mutableListOf())

    fun onSearchClick() {
        loadedPageIndex = 0
        availablePages = 0
        artistName = searchQuery
        artistsLiveData.value = mutableListOf()
        searchMoreArtists(searchQuery)
    }

    fun retry() {
//        loadedPageIndex = 0
        searchMoreArtists()
    }

    fun onArtistClick(artist: Artist?) {
        openTopAlbumsLiveData.value = artist?.name
    }

    val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            loadedPageIndex = 0
            artistName = query
            searchMoreArtists(query)
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            searchQuery = newText
            return false
        }
    }

    fun searchMoreArtists(artist: String? = artistName) {

        artist?.let {

            viewModelScope.launch(Dispatchers.Main) {

                networkState.value = NetworkState.Loading

                when (val resultTopAlbums =
                    searchRepository.searchArtist(
                        page = (loadedPageIndex + 1).toString(),
                        artist = artist
                    )) {

                    is Result.Success -> {
                        networkState.value = NetworkState.Loaded
                        setPageInfo(resultTopAlbums.data.results)
                        handleSearchResult(resultTopAlbums.data.results)
                    }

                    is Result.Error -> {
                        networkState.value =
                            NetworkState.Failure(null /*resultTopAlbums.errorCode*/)
                    }

                    is Result.Failure -> {
                        networkState.value = NetworkState.Failure(resultTopAlbums.error)
                    }
                }
            }

        }
    }

    private fun handleSearchResult(results: Results?) {

        if (results?.artistmatches?.artist == null) {
            return
        }

        var artists: MutableList<Artist>? = artistsLiveData.value
        artists?.addAll(results.artistmatches.artist)

        artistsLiveData.value = artists
    }


    private fun setPageInfo(results: Results?) {

        this.artistName = results?.opensearchQuery?.searchTerms

        val startPage = results?.opensearchQuery?.startPage?.toInt() ?: 0
        loadedPageIndex = startPage

        val itemsPerPage = results?.openSearchItemsPerPage?.toInt() ?: 50
        val totalResults = results?.openSearchTotalResults?.toInt() ?: 0
        var availablePages = totalResults / itemsPerPage
        //TODO Last.fm API Bug
        if (availablePages > 200) {
            availablePages = 200
        }
        this.availablePages = availablePages
    }


    fun hasExtraRow(): Boolean {
        return loadedPageIndex == 0 || loadedPageIndex < availablePages
    }

}