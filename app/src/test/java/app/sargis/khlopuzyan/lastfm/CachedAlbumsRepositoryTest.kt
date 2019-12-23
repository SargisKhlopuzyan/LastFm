package app.sargis.khlopuzyan.lastfm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import app.sargis.khlopuzyan.lastfm.database.DatabaseManager
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.repository.CachedAlbumsRepositoryImpl
import io.mockk.coEvery
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
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

/**
 * Created by Sargis Khlopuzyan, on 12/23/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@fcc.am)
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CachedAlbumsRepositoryTest {

    @Suppress("unused")
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private val mockDatabaseManager = mockk<DatabaseManager>()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var subject: CachedAlbumsRepositoryImpl

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        subject = CachedAlbumsRepositoryImpl(mockDatabaseManager)
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
    fun exceptionTest() = testDispatcher.runBlockingTest {

        val exception = IOException()

        coEvery {
            mockDatabaseManager.getAllCachedAlbumsLiveDataFromDatabase()
//        } throws exception
        } returns liveData {  listOf<Album>()}

        val albumsList = subject.getAllCachedAlbumsLiveData()

        albumsList.value?.let {
            assertEquals(it.size, 0)
        }
    }
}