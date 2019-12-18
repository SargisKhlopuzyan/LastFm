package app.sargis.khlopuzyan.lastfm.repository

import app.sargis.khlopuzyan.lastfm.database.DatabaseManager
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
interface CachedAlbumsRepository {
    suspend fun getAllAlbums(): List<Album>?
}

/**
 * Repository implementation for doing database query.
 */
class CachedAlbumsRepositoryImpl(
    private val databaseManager: DatabaseManager,
    private val coroutineScope: CoroutineScope
) : CachedAlbumsRepository {

    override suspend fun getAllAlbums(): List<Album>? =
        withContext(coroutineScope.coroutineContext) {
            return@withContext databaseManager.getAllAlbumsFromDatabase()
        }
}