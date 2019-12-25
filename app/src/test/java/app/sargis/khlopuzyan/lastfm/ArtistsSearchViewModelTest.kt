package app.sargis.khlopuzyan.lastfm

import android.view.View

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.sargis.khlopuzyan.lastfm.model.artists_search.Artist
import app.sargis.khlopuzyan.lastfm.model.artists_search.ResultArtists
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.repository.ArtistsSearchRepository
import app.sargis.khlopuzyan.lastfm.ui.artists_search.ArtistsSearchViewModel
import app.sargis.khlopuzyan.lastfm.util.DataLoadingState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

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
    private val mockResult = mockk<Result<ResultArtists>>(relaxed = true)
    private val mockResponse = mockk<Response<ResultArtists>>(relaxed = true)
    private val mockArtistsJson = mockk<ResultArtists>(relaxed = true)

    private val mockView = mockk<View>()

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
    fun errorRetryClickSearchArtistTest() = testDispatcher.runBlockingTest {

        every {
            mockResponse.isSuccessful
        } returns false

        coEvery {
            mockArtistsSearchRepository.searchArtist(artist = any())
        } returns mockResult

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

        assertEquals(dataLoadingState, DataLoadingState.Loading)
        assertEquals(artists, mutableListOf<Artist>())
    }

    /**
     * RetryClick invokes searchArtist
     * */
    @Test
    fun successSearchMoreArtistsTest() = testDispatcher.runBlockingTest {

        every {
            mockResponse.isSuccessful
        } returns true

        coEvery {
            mockArtistsSearchRepository.searchArtist(artist = any())
        } returns Result.Success(mockArtistsJson)

        val artistName = "Cher"
        subject.searchMoreArtists(artist = artistName)

        advanceTimeBy(1000)

        val dataLoadingStateObserver = LoggingObserver<DataLoadingState>()
        val errorMessageObserver = LoggingObserver<String>()

        subject.dataLoadingStateLiveData.observeForever(dataLoadingStateObserver)
        subject.errorMessageLiveData.observeForever(errorMessageObserver)

        val dataLoadingState = dataLoadingStateObserver.lastValue
        val errorMessage = errorMessageObserver.lastValue

        assertEquals(dataLoadingState, DataLoadingState.Loaded)
        assertEquals(errorMessage, null)
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