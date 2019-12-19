package app.sargis.khlopuzyan.lastfm.database.converter

import androidx.room.TypeConverter
import app.sargis.khlopuzyan.lastfm.util.CachedState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Sargis Khlopuzyan, on 12/18/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
class ItemDatabaseStateConverter {

    private val gson = Gson()

    @TypeConverter
    fun stringToItemDatabaseState(data: String?): CachedState {

        if (data == null) {
            return CachedState.NotCached
        }

        val type = object : TypeToken<CachedState>() {}.type

        return gson.fromJson<CachedState>(data, type)
    }

    @TypeConverter
    fun listToString(someObjects: CachedState): String {
        return gson.toJson(someObjects)
    }

}