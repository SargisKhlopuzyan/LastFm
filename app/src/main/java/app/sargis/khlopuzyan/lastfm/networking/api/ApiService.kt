package app.sargis.khlopuzyan.lastfm.networking.api

import app.sargis.khlopuzyan.lastfm.model.album_info.ResultAlbumInfo
import app.sargis.khlopuzyan.lastfm.model.artists_search.Result
import app.sargis.khlopuzyan.lastfm.model.top_albums.ResultTopAlbums
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
interface ApiService {

    @GET("?")
    suspend fun searchArtist(
        @Query(value = "method") method: String = "artist.search",
        @Query(value = "page") page: String = "1",
        @Query(value = "artist") artist: String,
        @Query(value = "format") format: String = "json"
    ): Response<Result>


    @GET("?")
    suspend fun searchTopAlbums(
        @Query(value = "method") method: String = "artist.gettopalbums",
        @Query(value = "page") page: String = "1",
        @Query(value = "artist") artist: String,
        @Query(value = "format") format: String = "json"
    ): Response<ResultTopAlbums>


    @GET("?")
    suspend fun searchAlbumInfo(
        @Query(value = "method") method: String = "album.getinfo",
        @Query(value = "artist") artist: String,
        @Query(value = "album") album: String,
        @Query(value = "format") format: String = "json"
    ): Response<ResultAlbumInfo>

}