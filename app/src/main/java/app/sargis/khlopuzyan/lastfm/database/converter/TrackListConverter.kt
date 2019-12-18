package app.sargis.khlopuzyan.lastfm.database.converter

import androidx.room.TypeConverter
import app.sargis.khlopuzyan.lastfm.model.album_info.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
class TrackListConverter {

    private val gson = Gson()

    @TypeConverter
    fun stringToList(data: String?): List<Track> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Track>>() {

        }.type

        return gson.fromJson<List<Track>>(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<Track>): String {
        return gson.toJson(someObjects)
    }

}