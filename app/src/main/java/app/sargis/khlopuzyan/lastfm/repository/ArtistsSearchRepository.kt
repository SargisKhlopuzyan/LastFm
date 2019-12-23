package app.sargis.khlopuzyan.lastfm.repository

import app.sargis.khlopuzyan.lastfm.model.artists_search.ResultArtists
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
interface ArtistsSearchRepository {
    suspend fun searchArtist(
        page: String = "1",
        artist: String
    ): Result<ResultArtists>
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
    ): Result<ResultArtists> =
        withContext(coroutineScope.coroutineContext) {

            try {
                val response: Response<ResultArtists> =
                    apiService.searchArtist(page = page, artist = artist)

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

//                return@withContext apiService.searchArtist(page = page, artist = artist).getResult()
            } catch (ex: Exception) {
                return@withContext Result.Failure(ex)
            }
        }
}