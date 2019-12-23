package app.sargis.khlopuzyan.lastfm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.sargis.khlopuzyan.lastfm.database.DatabaseManager
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.repository.CachedAlbumsRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Sargis Khlopuzyan, on 12/23/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@fcc.am)
 */
class CachedAlbumsRepositoryTest {

    private val mockDatabaseManager = mockk<DatabaseManager>()
    private lateinit var subject: CachedAlbumsRepositoryImpl

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        subject = CachedAlbumsRepositoryImpl(mockDatabaseManager)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getAllCachedAlbumsLiveDataFromDatabaseTest() {

        var allCachedAlbumsLiveData: LiveData<List<Album>?> = MutableLiveData()
        every {
            mockDatabaseManager.getAllCachedAlbumsLiveDataFromDatabase()
        } returns allCachedAlbumsLiveData

        val albumsLiveData = subject.getAllCachedAlbumsLiveData()

        assertEquals(albumsLiveData, allCachedAlbumsLiveData)
    }
}