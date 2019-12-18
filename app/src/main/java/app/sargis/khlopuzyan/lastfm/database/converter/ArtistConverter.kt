package app.sargis.khlopuzyan.lastfm.database.converter

import androidx.room.TypeConverter
import app.sargis.khlopuzyan.lastfm.model.top_albums.Artist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
class ArtistConverter {

    private val gson = Gson()

    @TypeConverter
    fun stringToArtist(data: String?): Artist {

        if (data == null) {
            return Artist()
        }

        val type = object : TypeToken<Artist>() {}.type

        return gson.fromJson<Artist>(data, type)
    }

    @TypeConverter
    fun listToString(someObjects: Artist): String {
        return gson.toJson(someObjects)
    }

}