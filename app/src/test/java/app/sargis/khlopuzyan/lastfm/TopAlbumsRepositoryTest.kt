package app.sargis.khlopuzyan.lastfm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.sargis.khlopuzyan.lastfm.database.DatabaseManager
import app.sargis.khlopuzyan.lastfm.model.album_info.ResultAlbumInfo
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.model.top_albums.ResultTopAlbums
import app.sargis.khlopuzyan.lastfm.networking.api.ApiService
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.repository.TopAlbumsRepositoryImpl
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
import org.junit.*
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
class TopAlbumsRepositoryTest {

    @Suppress("unused")
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private val mockDatabaseManager = mockk<DatabaseManager>()

    private val testDispatcher = TestCoroutineDispatcher()
    private val mockApi = mockk<ApiService>(relaxed = true)
    private val mockResponseTopAlbums = mockk<Response<ResultTopAlbums>>(relaxed = true)
    private val mockResponseAlbumInfo = mockk<Response<ResultAlbumInfo>>(relaxed = true)
    private val mockResultTopAlbums = mockk<ResultTopAlbums>()
    private val mockResultAlbumInfo = mockk<ResultAlbumInfo>()

    private lateinit var subject: TopAlbumsRepositoryImpl

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        subject = TopAlbumsRepositoryImpl(mockApi, mockDatabaseManager, MainScope())
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Tests top albums requests

    /**
     * When request fails with an exception
     * Then emits [Result.Failure]
     */
    @Test
    fun exceptionRequestTopAlbumsTest() = testDispatcher.runBlockingTest {

        val exception = IOException()

        coEvery {
            mockApi.searchArtist(page = any(), artist = any())
        } throws exception

        val page = "1"
        val artistName = "Cher"
        val result = subject.searchTopAlbums(
            page = page, artist = artistName
        )

        assert(result is Result.Failure)
    }

    /**
     * When request fails with an error
     * Then emits [Result.Error]
     */
    @Test
    fun errorRequestTopAlbumsTest() = testDispatcher.runBlockingTest {

        every {
            mockResponseTopAlbums.isSuccessful
        } returns false

        coEvery {
            mockApi.searchTopAlbums(page = any(), artist = any())
        } returns mockResponseTopAlbums

        val page = "1"
        val artistName = "Cher"
        val result = subject.searchTopAlbums(
            page = page, artist = artistName
        )

        assert(result is Result.Error)
    }

    /**
     * When request is successful
     * Then emits [Result.Success] with ResultTopAlbums
     */
    @Test
    fun successRequestTopAlbumsTest() = testDispatcher.runBlockingTest {

        coEvery {
            mockApi.searchTopAlbums(page = any(), artist = any())
        } returns Response.success(mockResultTopAlbums)

        val page = "1"
        val artistName = "Cher"
        val result = subject.searchTopAlbums(
            page = page, artist = artistName
        )

        assert(result is Result.Success)
        Assert.assertEquals((result as Result.Success).data, mockResultTopAlbums)
    }


    // Tests album info requests

    /**
     * When request fails with an exception
     * Then emits [Result.Failure]
     */
    @Test
    fun exceptionRequestAlbumInfoTest() = testDispatcher.runBlockingTest {

        val exception = IOException()

        coEvery {
            mockApi.searchAlbumInfo(artist = any(), album = any())
        } throws exception

        val artistName = "Cher"
        val albumName = "Believe"
        val result = subject.searchAlbumInfo(
            artist = artistName, album = albumName
        )

        assert(result is Result.Failure)
    }

    /**
     * When request fails with an error
     * Then emits [Result.Error]
     */
    @Test
    fun errorRequestAlbumInfoTest() = testDispatcher.runBlockingTest {

        every {
            mockResponseTopAlbums.isSuccessful
        } returns false

        coEvery {
            mockApi.searchAlbumInfo(artist = any(), album = any())
        } returns mockResponseAlbumInfo

        val artistName = "Cher"
        val albumName = "Believe"
        val result = subject.searchAlbumInfo(
            artist = artistName, album = albumName
        )

        assert(result is Result.Error)
    }

    /**
     * When request is successful
     * Then emits [Result.Success] with ResultTopAlbums
     */
    @Test
    fun successRequestAlbumInfoTest() = testDispatcher.runBlockingTest {

        coEvery {
            mockApi.searchAlbumInfo(artist = any(), album = any())
        } returns Response.success(mockResultAlbumInfo)

        val artistName = "Cher"
        val albumName = "Believe"
        val result = subject.searchAlbumInfo(
            artist = artistName, album = albumName
        )

        assert(result is Result.Success)
        Assert.assertEquals((result as Result.Success).data, mockResultAlbumInfo)
    }

    // Tests database functions

    /**
     * Tests fetching all top albums from cache
     * **/
    @Test
    fun getAllTopAlbumsFromCacheTest() = testDispatcher.runBlockingTest {

        var allCachedAlbums: List<Album> = listOf()
        every {
            mockDatabaseManager.getAllCachedAlbumsFromDatabase()
        } returns allCachedAlbums

        val albums = subject.getAllTopAlbumsFromCache()

        Assert.assertEquals(albums, allCachedAlbums)
    }

    /**
     * Tests saving top album in cache
     * **/
    @Test
    fun saveTopAlbumInCacheTest() = testDispatcher.runBlockingTest {

        var id = 1.toLong()
        every {
            mockDatabaseManager.saveAlbumInDatabase(album = any())
        } returns id

        val album = Album()
        val savedId = subject.saveTopAlbumInCache(album = album)

        Assert.assertEquals(id, savedId)
    }

    /**
     * Tests deleting top album from cache
     * **/
    @Test
    fun deleteTopAlbumFromCacheTest() = testDispatcher.runBlockingTest {

        var id = 1
        every {
            mockDatabaseManager.deleteAlbumFromDatabase(album = any())
        } returns id

        val album = Album()
        val deleteId = subject.deleteTopAlbumFromCache(album = album)

        Assert.assertEquals(id, deleteId)
    }

}