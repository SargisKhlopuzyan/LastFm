package app.sargis.khlopuzyan.lastfm.database.converter

import androidx.room.TypeConverter
import app.sargis.khlopuzyan.lastfm.model.top_albums.Image
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
class ImageListConverter {

    private val gson = Gson()

    @TypeConverter
    fun stringToList(data: String?): List<Image> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Image>>() {

        }.type

        return gson.fromJson<List<Image>>(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<Image>): String {
        return gson.toJson(someObjects)
    }

}