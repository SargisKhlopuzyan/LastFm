package app.sargis.khlopuzyan.lastfm

import app.sargis.khlopuzyan.lastfm.database.DatabaseManager
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.repository.AlbumDetailsRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Sargis Khlopuzyan, on 12/25/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@fcc.am)
 */
class AlbumDetailsRepositoryTest {

    private val mockDatabaseManager = mockk<DatabaseManager>()
    private lateinit var subject: AlbumDetailsRepositoryImpl

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        subject = AlbumDetailsRepositoryImpl(mockDatabaseManager)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun saveTopAlbumInCacheTest() {

        every {
            mockDatabaseManager.saveAlbumInDatabase(album = any())
        } returns 1.toLong()

        val album = Album()
        val databaseId = subject.saveTopAlbumInCache(album)

        assertEquals(databaseId, 1)
    }

    @Test
    fun deleteTopAlbumFromCacheTest() {

        every {
            mockDatabaseManager.deleteAlbumFromDatabase(album = any())
        } returns 1

        val album = Album()
        val result = subject.deleteTopAlbumFromCache(album)

        assertEquals(result, 1)
    }

}