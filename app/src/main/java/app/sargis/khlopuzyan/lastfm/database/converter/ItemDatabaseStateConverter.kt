package app.sargis.khlopuzyan.lastfm.database.converter

import androidx.room.TypeConverter
import app.sargis.khlopuzyan.lastfm.util.AlbumCacheState
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
    fun stringToItemDatabaseState(data: String?): AlbumCacheState {

        if (data == null) {
            return AlbumCacheState.NotCached
        }

        val type = object : TypeToken<AlbumCacheState>() {}.type

        return gson.fromJson<AlbumCacheState>(data, type)
    }

    @TypeConverter
    fun listToString(someObjects: AlbumCacheState): String {
        return gson.toJson(someObjects)
    }

}