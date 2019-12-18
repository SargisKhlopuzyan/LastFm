package app.sargis.khlopuzyan.lastfm.repository

import app.sargis.khlopuzyan.lastfm.networking.api.ApiService
import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import app.sargis.khlopuzyan.lastfm.networking.helper.getResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
interface ArtistsSearchRepository {
    suspend fun searchArtist(
        page: String = "1",
        artist: String
    ): Result<app.sargis.khlopuzyan.lastfm.model.artists_search.Result>
}

/**
 * Repository implementation for doing artists search.
 */
class ArtistsSearchRepositoryImpl(
    private val apiService: ApiService,
    private val coroutineScope: CoroutineScope
) : ArtistsSearchRepository {

    override suspend fun searchArtist(
        page: String,
        artist: String
    ): Result<app.sargis.khlopuzyan.lastfm.model.artists_search.Result> =
        withContext(coroutineScope.coroutineContext) {
            try {
                return@withContext apiService.searchArtist(page = page, artist = artist).getResult()
            } catch (ex: Exception) {
                return@withContext Result.Failure(ex)
            }
        }
}