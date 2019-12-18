package app.sargis.khlopuzyan.lastfm.model.album_info

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */

@JsonClass(generateAdapter = true)
data class ResultAlbumInfo(

    @Json(name = "album")
    val album: Album? = null

)

@JsonClass(generateAdapter = true)
data class Album(

    @Json(name = "artist")
    val artist: String? = null,

    @Json(name = "image")
    val image: List<Image>? = null,

    @Json(name = "listeners")
    val listeners: String? = null,

    @Json(name = "mbid")
    val mbid: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "playcount")
    val playcount: String? = null,

    @Json(name = "tags")
    val tags: Tags? = null,

    @Json(name = "tracks")
    val tracks: Tracks? = null,

    @Json(name = "url")
    val url: String? = null,

    @Json(name = "wiki")
    val wiki: Wiki? = null

)

@JsonClass(generateAdapter = true)
data class Image(

    @Json(name = "#text")
    val text: String? = null,

    @Json(name = "size")
    val size: String? = null

)

@JsonClass(generateAdapter = true)
data class Tags(

    @Json(name = "tag")
    val tag: List<Tag> = listOf()

)

@JsonClass(generateAdapter = true)
data class Tag(

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "url")
    val url: String? = null

)

@JsonClass(generateAdapter = true)
data class Tracks(

    @Json(name = "track")
    val track: List<Track> = listOf()

)

@Parcelize
@JsonClass(generateAdapter = true)
data class Track(

    @Json(name = "@attr")
    val attr: Attr? = null,

    @Json(name = "artist")
    val artist: Artist? = null,

    @Json(name = "duration")
    val duration: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "streamable")
    val streamable: Streamable? = null,

    @Json(name = "url")
    val url: String? = null

) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Attr(

    @Json(name = "rank")
    val rank: String? = null

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
data class Streamable(

    @Json(name = "#text")
    val text: String? = null,

    @Json(name = "fulltrack")
    val fulltrack: String? = null

) : Parcelable

@JsonClass(generateAdapter = true)
data class Wiki(

    @Json(name = "content")
    val content: String? = null,

    @Json(name = "published")
    val published: String? = null,

    @Json(name = "summary")
    val summary: String? = null

)