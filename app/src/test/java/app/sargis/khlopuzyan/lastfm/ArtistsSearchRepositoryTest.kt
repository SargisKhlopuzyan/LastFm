package app.sargis.khlopuzyan.lastfm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.sargis.khlopuzyan.lastfm.model.artists_search.ResultArtists
import app.sargis.khlopuzyan.lastfm.networking.api.ApiService
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.repository.ArtistsSearchRepositoryImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.io.IOException

/**
 * Created by Sargis Khlopuzyan, on 12/23/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@fcc.am)
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ArtistsSearchRepositoryTest {

    @Suppress("unused")
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val mockApi = mockk<ApiService>()
    private val mockResponse = mockk<Response<ResultArtists>>()
    private val mockResult = mockk<ResultArtists>()
    private val mockResponseBody = mockk<ResponseBody>()

    private lateinit var subject: ArtistsSearchRepositoryImpl

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        subject = ArtistsSearchRepositoryImpl(mockApi, MainScope())
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * When request fails with an exception
     * Then emits [Result.Failure]
     */
    @Test
    fun exceptionRequestTest() = testDispatcher.runBlockingTest {

        val page = "1"
        val artistName = "Cher"

        val exception = IOException()

        coEvery {
            mockApi.searchArtist(page = page, artist = artistName)
        } throws exception

        val resultArtists = subject.searchArtist(
            page = page, artist = artistName
        )

        assert(resultArtists is Result.Failure)
    }

    /**
     * When request fails with an error
     * Then emits [Result.Error]
     */
    @Test
    fun errorRequestTest() = testDispatcher.runBlockingTest {

        val page = "1"
        val artistName = "Cher"

        every {
            mockResponse.isSuccessful
        } returns false

        every {
            mockResponse.code()
        } returns 2

        every {
            mockResponse.errorBody()
        } returns mockResponseBody

        every {
            mockResponse.message()
        } returns "Invalid service - This service does not exist"


        coEvery {
            mockApi.searchArtist(page = page, artist = artistName)
        } returns mockResponse

        val result = subject.searchArtist(
            page = page, artist = artistName
        )

        assert(result is Result.Error)
    }

    /**
     * When request is successful
     * Then emits [Result.Success] with ResultArtists
     */
    @Test
    fun successRequestTest() = testDispatcher.runBlockingTest {

        val page = "1"
        val artistName = "Cher"

        coEvery {
            mockApi.searchArtist(page = page, artist = artistName)
        } returns Response.success(mockResult)

        val resultArtists = subject.searchArtist(
            page = page, artist = artistName
        )

        assert(resultArtists is Result.Success)
    }

}