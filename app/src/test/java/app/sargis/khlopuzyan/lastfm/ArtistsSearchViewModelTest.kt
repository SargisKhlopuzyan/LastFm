package app.sargis.khlopuzyan.lastfm

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.sargis.khlopuzyan.lastfm.model.artists_search.ResultArtists
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.repository.ArtistsSearchRepository
import app.sargis.khlopuzyan.lastfm.ui.artists_search.ArtistsSearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
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

    private lateinit var subjectArtistsSearchViewModel: ArtistsSearchViewModel

    private val mockArtistsSearchRepository = mockk<ArtistsSearchRepository>()

    private val mockArtistsSearchJson = mockk<ResultArtists>()
    private val mockView = mockk<View>()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        subjectArtistsSearchViewModel = ArtistsSearchViewModel(mockArtistsSearchRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * RetryClick invokes searchArtist
     * */
    @Test
    fun retryClickTest() = testDispatcher.runBlockingTest {

        val name = "Cher"

        coEvery {
            mockArtistsSearchRepository.searchArtist(artist = name)
        } returns Result.Success(mockArtistsSearchJson)

//        subjectArtistsSearchViewModel.searchMoreArtists(artist = name)
//        coVerify(exactly = 2) {
//            mockArtistsSearchRepository.searchArtist(artist = name)
//        }
    }

//    /**
//     * RetryClick invokes searchMoreArtists
//     * */
//    @Test
//    fun searchMoreArtistsTest() = testDispatcher.runBlockingTest {
//
//        val name = "Cher"
//
//        coEvery {
//            subjectArtistsSearchViewModel.searchMoreArtists(artist = name)
//        }
//    }

}