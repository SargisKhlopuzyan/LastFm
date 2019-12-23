package app.sargis.khlopuzyan.lastfm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.sargis.khlopuzyan.lastfm.database.converter.ArtistConverter
import app.sargis.khlopuzyan.lastfm.database.converter.ImageListConverter
import app.sargis.khlopuzyan.lastfm.database.converter.ItemDatabaseStateConverter
import app.sargis.khlopuzyan.lastfm.database.converter.TrackListConverter
import app.sargis.khlopuzyan.lastfm.database.dao.LastFmDAO
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album

/**
 * Created by Sargis Khlopuzyan, on 12/17/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
@TypeConverters(
    ImageListConverter::class,
    TrackListConverter::class,
    ArtistConverter::class,
    ItemDatabaseStateConverter::class
)
@Database(entities = [Album::class], version = 1, exportSchema = false)
abstract class LastFmDatabase : RoomDatabase() {

    abstract fun getLastFmDAO(): LastFmDAO

    companion object {

        @Volatile
        private var INSTANCE: LastFmDatabase? = null

        fun getInstance(context: Context): LastFmDatabase {

            if (INSTANCE == null) {

                synchronized(LastFmDatabase::class) {

                    if (INSTANCE == null) {

                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            LastFmDatabase::class.java, "last_fm.db"
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}