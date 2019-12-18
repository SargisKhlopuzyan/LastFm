package app.sargis.khlopuzyan.lastfm.database

import android.content.Context
import androidx.lifecycle.LiveData
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import javax.inject.Inject

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
class DatabaseManager @Inject constructor(var context: Context) {

    fun saveAlbumInDatabase(album: Album): Long {
        return LastFmDatabase.getInstance(context).getLastFmDAO().insertAlbum(album)
    }

    fun deleteAlbumFromDatabase(album: Album): Int {
        return LastFmDatabase.getInstance(context).getLastFmDAO().deleteAlbum(album.name)
    }

    fun getAlbumFromDatabase(name: String): Album? {
        return LastFmDatabase.getInstance(context).getLastFmDAO().getAlbumByName(name)
    }

    fun getAllMatchedAlbumsFromDatabase(artistName: String): List<Album> {
        return LastFmDatabase.getInstance(context).getLastFmDAO().getAllMatchedAlbums(artistName)
    }

    fun getAllCachedAlbumsFromDatabase(): List<Album> {
        return LastFmDatabase.getInstance(context).getLastFmDAO().getAllCachedAlbums()
    }

    fun getAllCachedAlbumsLiveDataFromDatabase(): LiveData<List<Album>?> {
        return LastFmDatabase.getInstance(context).getLastFmDAO().getAllCachedAlbumsLiveData()
    }

}