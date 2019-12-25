package app.sargis.khlopuzyan.lastfm.ui.artists_search

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.sargis.khlopuzyan.lastfm.helper.SingleLiveEvent
import app.sargis.khlopuzyan.lastfm.model.artists_search.Artist
import app.sargis.khlopuzyan.lastfm.model.artists_search.Results
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.repository.ArtistsSearchRepository
import app.sargis.khlopuzyan.lastfm.util.DataLoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistsSearchViewModel constructor(
    private val searchRepository: ArtistsSearchRepository
) : ViewModel() {

    private var artistName: String = ""
    private var availablePages: Int = 0
    private var loadedPageIndex: Int = 0
    private var searchQuery: String = ""

    val openTopAlbumsLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    val hideKeyboardLiveData: SingleLiveEvent<View> = SingleLiveEvent()
    val showToastLiveData: SingleLiveEvent<String> = SingleLiveEvent()

    val dataLoadingStateLiveData = MutableLiveData<DataLoadingState>()

    val artistsLiveData = MutableLiveData<MutableList<Artist>>(mutableListOf())
    val errorMessageLiveData = MutableLiveData<String>()

    /**
     * Handles search icon click
     * */
    fun onSearchClick(v: View) {
        hideKeyboardLiveData.value = v
        loadedPageIndex = 0
        availablePages = 0
        artistName = searchQuery
        artistsLiveData.value = mutableListOf()
        searchMoreArtists(searchQuery)
    }

    /**
     * Handles retry icon click
     * */
    fun retryClick(v: View) {
        hideKeyboardLiveData.value = v
        searchMoreArtists()
    }

    /**
     * Handles artist list item click
     * */
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

    /**
     * Loads api next page artists
     * */
    fun searchMoreArtists(artist: String? = artistName) {

        artist?.let {

            viewModelScope.launch(Dispatchers.Main) {

                dataLoadingStateLiveData.value = DataLoadingState.Loading

                val resultArtists = searchRepository.searchArtist(
                    page = (loadedPageIndex + 1).toString(),
                    artist = artist
                )

                when (resultArtists) {

                    is Result.Success -> {
                        dataLoadingStateLiveData.value = DataLoadingState.Loaded
                        setApiPageInfo(resultArtists.data.results)
                        handleSearchResult(resultArtists.data.results)
                    }

                    is Result.Error -> {
                        errorMessageLiveData.value =
                            "Something went wrong.\nError code: ${resultArtists.errorCode}"
                        dataLoadingStateLiveData.value =
                            DataLoadingState.Failure(null /*resultTopAlbums.errorCode*/)
                    }

                    is Result.Failure -> {
                        errorMessageLiveData.value =
                            "Something went wrong.\nCheck your internet connection"
                        dataLoadingStateLiveData.value =
                            DataLoadingState.Failure(resultArtists.error)
                    }
                }
            }
        }
    }

    /**
     * Handles search result
     * */
    private fun handleSearchResult(results: Results?) {

        var artists: MutableList<Artist>?
        if (results?.artistMatches?.artist == null) {
            artists = mutableListOf()
        } else {
            artists = artistsLiveData.value
            artists?.addAll(results.artistMatches.artist)
        }

        artistsLiveData.value = artists
    }

    /**
     * Sets api info (loaded page index, available pages, etc)
     *
     * @param results results returned from api call
     * */
    private fun setApiPageInfo(results: Results?) {

        this.artistName = results?.openSearchQuery?.searchTerms ?: ""

        val startPage = try {
            results?.openSearchQuery?.startPage?.toInt() ?: 0
        } catch (ex: NumberFormatException) {
            0
        }

        loadedPageIndex = startPage

        val itemsPerPage =
            try {
                results?.openSearchItemsPerPage?.toInt() ?: 50
            } catch (ex: NumberFormatException) {
                50
            }

        val totalResults =
            try {
                results?.openSearchTotalResults?.toInt() ?: 0
            } catch (ex: NumberFormatException) {
                50
            }

        var availablePages = totalResults / itemsPerPage
        //TODO Last.fm API Bug
        if (availablePages > 200) {
            availablePages = 200
        }
        this.availablePages = availablePages
    }

    /**
     * Checks weather api has pages available
     * */
    fun hasExtraRow(): Boolean =
        (dataLoadingStateLiveData.value != null && dataLoadingStateLiveData.value != DataLoadingState.Loaded) || (loadedPageIndex < availablePages)

}