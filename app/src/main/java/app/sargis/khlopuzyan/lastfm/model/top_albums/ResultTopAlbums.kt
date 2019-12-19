package app.sargis.khlopuzyan.lastfm.model.top_albums

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.sargis.khlopuzyan.lastfm.model.album_info.Track
import app.sargis.khlopuzyan.lastfm.util.CachedState
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
@JsonClass(generateAdapter = true)
data class ResultTopAlbums(

    @Json(name = "topalbums")
    val topAlbums: TopAlbums? = null

)

@JsonClass(generateAdapter = true)
data class TopAlbums(

    @Json(name = "@attr")
    val attr: Attr? = null,

    @Json(name = "album")
    val albums: List<Album> = listOf()

)

@JsonClass(generateAdapter = true)
data class Attr(

    @Json(name = "artist")
    val artist: String? = null,

    @Json(name = "page")
    val page: String? = null,

    @Json(name = "perPage")
    val perPage: String? = null,

    @Json(name = "total")
    val total: String? = null,

    @Json(name = "totalPages")
    val totalPages: String? = null

)

@Entity(tableName = "albums")
@Parcelize
@JsonClass(generateAdapter = true)
data class Album(

    @PrimaryKey(autoGenerate = true)
    var dbRowId: Long = 0.toLong(),

    @Json(name = "artist")
    val artist: Artist? = null,

    @Json(name = "image")
    val image: List<Image> = listOf(),

    @Json(name = "mbid")
    val mbid: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "playcount")
    val playCount: Int? = null,

    @Json(name = "url")
    val url: String? = null,

    var tracks: List<Track>? = listOf(),

    var cachedState: CachedState = CachedState.NotCached

) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Artist(

    @Json(name = "mbid")
    val mbid: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "url")
    val url: String? = null

) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Image(

    @Json(name = "#text")
    val text: String? = null,

    @Json(name = "size")
    val size: String? = null

) : Parcelable