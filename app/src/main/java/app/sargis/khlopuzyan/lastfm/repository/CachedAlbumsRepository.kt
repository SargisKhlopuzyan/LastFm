package app.sargis.khlopuzyan.lastfm.repository

import androidx.lifecycle.LiveData
import app.sargis.khlopuzyan.lastfm.database.DatabaseManager
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
interface CachedAlbumsRepository {
    fun getAllCachedAlbumsLiveData(): LiveData<List<Album>?>
}

/**
 * Repository implementation for doing database query.
 */
class CachedAlbumsRepositoryImpl(
    private val databaseManager: DatabaseManager
) : CachedAlbumsRepository {

    override fun getAllCachedAlbumsLiveData(): LiveData<List<Album>?> {
        return databaseManager.getAllCachedAlbumsLiveDataFromDatabase()
    }
}