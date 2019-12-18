package app.sargis.khlopuzyan.lastfm.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album

/**
 * Created by Sargis Khlopuzyan, on 12/18/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
@Dao
interface LastFmDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbum(item: Album): Long

    @Update
    fun updateAlbum(vararg items: Album): Int

    @Query("DELETE FROM albums WHERE name = :name")
    fun deleteAlbum(name: String?): Int

    @Query("SELECT * FROM albums WHERE name = :albumName")
    fun getAlbumByName(albumName: String?): Album?

    @Query("SELECT * FROM albums WHERE artist LIKE :artistName")
    fun getAllMatchedAlbums(artistName: String?): List<Album>

    @Query("SELECT * FROM albums")
    fun getAllCachedAlbums(): List<Album>
    @Query("SELECT * FROM albums")

    fun getAllCachedAlbumsLiveData(): LiveData<List<Album>?>

}