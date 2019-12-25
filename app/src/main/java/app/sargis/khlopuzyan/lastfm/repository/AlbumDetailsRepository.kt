package app.sargis.khlopuzyan.lastfm.repository

import app.sargis.khlopuzyan.lastfm.database.DatabaseManager
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album

/**
 * Created by Sargis Khlopuzyan, on 12/24/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
interface AlbumDetailsRepository {
    fun saveTopAlbumInCache(album: Album): Long
    fun deleteTopAlbumFromCache(album: Album): Int
}

/**
 * Repository implementation for doing caching queries.
 */
class AlbumDetailsRepositoryImpl(
    private val databaseManager: DatabaseManager
) : AlbumDetailsRepository {

    override fun saveTopAlbumInCache(album: Album): Long {
        return databaseManager.saveAlbumInDatabase(album)
    }

    override fun deleteTopAlbumFromCache(album: Album): Int {
        return databaseManager.deleteAlbumFromDatabase(album)
    }

}