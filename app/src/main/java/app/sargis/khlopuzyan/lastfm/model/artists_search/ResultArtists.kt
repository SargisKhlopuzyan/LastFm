package app.sargis.khlopuzyan.lastfm.model.artists_search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
@JsonClass(generateAdapter = true)
data class Result(

    @Json(name = "results")
    val results: Results? = null

)

@JsonClass(generateAdapter = true)
data class Results(

    @Json(name = "@attr")
    val attr: Attr? = null,

    @Json(name = "artistmatches")
    val artistMatches: ArtistMatches? = null,

    @Json(name = "opensearch:Query")
    val openSearchQuery: OpenSearchQuery? = null,

    @Json(name = "opensearch:itemsPerPage")
    val openSearchItemsPerPage: String? = null,

    @Json(name = "opensearch:startIndex")
    val openSearchStartIndex: String? = null,

    @Json(name = "opensearch:totalResults")
    val openSearchTotalResults: String? = null

)

@JsonClass(generateAdapter = true)
data class Attr(

    @Json(name = "for")
    val `for`: String? = null

)

@JsonClass(generateAdapter = true)
data class ArtistMatches(

    @Json(name = "artist")
    val artist: List<Artist> = listOf()

)

@JsonClass(generateAdapter = true)
data class Artist(

    @Json(name = "image")
    val image: List<Image> = listOf(),

    @Json(name = "listeners")
    val listeners: String? = null,

    @Json(name = "mbid")
    val mbid: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "streamable")
    val streamable: String? = null,

    @Json(name = "url")
    val url: String? = null

)

@JsonClass(generateAdapter = true)
data class Image(

    @Json(name = "#text")
    val text: String? = null,

    @Json(name = "size")
    val size: String? = null

)

@JsonClass(generateAdapter = true)
data class OpenSearchQuery(

    @Json(name = "#text")
    val text: String? = null,

    @Json(name = "role")
    val role: String? = null,

    @Json(name = "searchTerms")
    val searchTerms: String? = null,

    @Json(name = "startPage")
    val startPage: String? = null

)