package app.sargis.khlopuzyan.lastfm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.sargis.khlopuzyan.lastfm.model.artists_search.Artist
import app.sargis.khlopuzyan.lastfm.model.artists_search.ResultArtists
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.repository.ArtistsSearchRepository
import app.sargis.khlopuzyan.lastfm.ui.artists_search.ArtistsSearchViewModel
import app.sargis.khlopuzyan.lastfm.util.DataLoadingState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*

/**
 * Created by Sargis Khlopuzyan, on 12/20/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@fcc.am)
 */
@ExperimentalCoroutinesApi
class ArtistsSearchViewModelTest {

    @Suppress("unused")
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var subject: ArtistsSearchViewModel

    private val mockArtistsSearchRepository = mockk<ArtistsSearchRepository>()
    private val mockResultArtists = mockk<ResultArtists>(relaxed = true)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        subject = ArtistsSearchViewModel(mockArtistsSearchRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * RetryClick invokes searchArtist
     * */
    @Test
    fun errorSearchMoreArtistsTest() = testDispatcher.runBlockingTest {

        val errorCode = 2

        coEvery {
            mockArtistsSearchRepository.searchArtist(artist = any())
        } returns Result.Error(errorCode, null)

        val dataLoadingStateObserver = LoggingObserver<DataLoadingState>()
        val errorMessageObserver = LoggingObserver<String>()
        val artistsObserver = LoggingObserver<MutableList<Artist>>()

        subject.dataLoadingStateLiveData.observeForever(dataLoadingStateObserver)
        subject.errorMessageLiveData.observeForever(errorMessageObserver)
        subject.artistsLiveData.observeForever(artistsObserver)

        val artistName = "Cher"
        subject.searchMoreArtists(artist = artistName)

        val dataLoadingState = dataLoadingStateObserver.lastValue
        val errorMessage = errorMessageObserver.lastValue
        val artists = artistsObserver.lastValue

        assert(dataLoadingState is DataLoadingState.Failure)
        assertEquals(errorMessage, "Something went wrong.\nError code: $errorCode")
        assert(artists == null || artists.isEmpty())
    }

    @Test
    fun failureSearchMoreArtistTest() = testDispatcher.runBlockingTest {

        coEvery {
            mockArtistsSearchRepository.searchArtist(artist = any())
        } returns Result.Failure(null)

        val dataLoadingStateObserver = LoggingObserver<DataLoadingState>()
        val errorMessageObserver = LoggingObserver<String>()
        val artistsObserver = LoggingObserver<MutableList<Artist>>()

        subject.dataLoadingStateLiveData.observeForever(dataLoadingStateObserver)
        subject.errorMessageLiveData.observeForever(errorMessageObserver)
        subject.artistsLiveData.observeForever(artistsObserver)

        val artistName = "Cher"
        subject.searchMoreArtists(artist = artistName)

        val dataLoadingState = dataLoadingStateObserver.lastValue
        val errorMessage = errorMessageObserver.lastValue
        val artists = artistsObserver.lastValue

        assert(dataLoadingState is DataLoadingState.Failure)
        assertEquals(errorMessage, "Something went wrong.\nCheck your internet connection")
        assert(artists == null || artists.isEmpty())
    }

    @Test
    fun successSearchMoreArtistsTest() = testDispatcher.runBlockingTest {

        coEvery {
            mockArtistsSearchRepository.searchArtist(artist = any())
        } returns Result.Success(mockResultArtists)

        val dataLoadingStateObserver = LoggingObserver<DataLoadingState>()
        val errorMessageObserver = LoggingObserver<String>()
        val artistsObserver = LoggingObserver<MutableList<Artist>>()

        subject.dataLoadingStateLiveData.observeForever(dataLoadingStateObserver)
        subject.errorMessageLiveData.observeForever(errorMessageObserver)
        subject.artistsLiveData.observeForever(artistsObserver)

        val artistName = "Cher"
        subject.searchMoreArtists(artist = artistName)

        val dataLoadingState = dataLoadingStateObserver.lastValue
        val errorMessage = errorMessageObserver.lastValue
        val artists = artistsObserver.lastValue

        assert(dataLoadingState is DataLoadingState.Loaded)
        assertEquals(errorMessage, null)
        Assert.assertEquals(mockResultArtists.results?.artistMatches?.artist?.size, artists?.size)
    }

    /**
     * Observer logs any values it receives
     */
    private class LoggingObserver<T> : Observer<T> {

        val lastValue: T?
            get() = try {
                allValues.last()
            } catch (ex: NoSuchElementException) {
                null
            }

        val allValues = ArrayList<T?>()

        val updatedCount
            get() = allValues.size

        override fun onChanged(t: T?) {
            allValues.add(t)
        }

    }

}