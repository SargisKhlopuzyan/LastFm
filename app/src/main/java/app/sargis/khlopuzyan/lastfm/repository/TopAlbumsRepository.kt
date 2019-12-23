package app.sargis.khlopuzyan.lastfm.repository

import app.sargis.khlopuzyan.lastfm.database.DatabaseManager
import app.sargis.khlopuzyan.lastfm.model.album_info.ResultAlbumInfo
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.model.top_albums.ResultTopAlbums
import app.sargis.khlopuzyan.lastfm.networking.api.ApiService
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.networking.helper.getResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
interface TopAlbumsRepository {

    suspend fun searchTopAlbums(page: String = "1", artist: String): Result<ResultTopAlbums>

    suspend fun searchAlbumInfo(artist: String, album: String): Result<ResultAlbumInfo>

    suspend fun getAllTopAlbumsFromCache(): List<Album>?

    suspend fun saveTopAlbumInCache(album: Album): Long

    suspend fun deleteTopAlbumFromCache(album: Album): Int
}

/**
 * Repository implementation for doing top albums search
 */
class TopAlbumsRepositoryImpl(
    private val apiService: ApiService,
    private val databaseManager: DatabaseManager,
    private val coroutineScope: CoroutineScope
) : TopAlbumsRepository {

    override suspend fun searchTopAlbums(page: String, artist: String): Result<ResultTopAlbums> =
        withContext(coroutineScope.coroutineContext) {
            try {

                val response: Response<ResultTopAlbums> =
                    apiService.searchTopAlbums(page = page, artist = artist)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        return@withContext response.getResult()

                    } else {
                        return@withContext Result.Error(response.code(), response.errorBody())
                    }
                } else {
                    return@withContext Result.Error(response.code(), response.errorBody())
                }

//                return@withContext apiService.searchTopAlbums(page = page, artist = artist).getResult()
            } catch (ex: Exception) {
                return@withContext Result.Failure(ex)
            }
        }

    override suspend fun searchAlbumInfo(artist: String, album: String): Result<ResultAlbumInfo> =
        withContext(coroutineScope.coroutineContext) {
            try {

                val response: Response<ResultAlbumInfo> =
                    apiService.searchAlbumInfo(artist = artist, album = album)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        return@withContext response.getResult()

                    } else {
                        return@withContext Result.Error(response.code(), response.errorBody())
                    }
                } else {
                    return@withContext Result.Error(response.code(), response.errorBody())
                }

//                return@withContext apiService.searchAlbumInfo(artist = artist, album = album).getResult()
            } catch (ex: Exception) {
                return@withContext Result.Failure(ex)
            }
        }

    override suspend fun getAllTopAlbumsFromCache(): List<Album> =
        databaseManager.getAllCachedAlbumsFromDatabase()

    override suspend fun saveTopAlbumInCache(album: Album): Long =
        databaseManager.saveAlbumInDatabase(album)

    override suspend fun deleteTopAlbumFromCache(album: Album): Int =
        databaseManager.deleteAlbumFromDatabase(album)

}