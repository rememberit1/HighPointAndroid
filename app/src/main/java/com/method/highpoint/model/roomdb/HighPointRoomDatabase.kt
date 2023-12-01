package com.method.highpoint.model.roomdb

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.method.highpoint.model.roomdb.event.EventRoomData
import kotlinx.coroutines.CoroutineScope

import com.method.highpoint.model.roomdb.area.AreaRoomData
import com.method.highpoint.model.roomdb.building.BuildingRoomData
import com.method.highpoint.model.roomdb.category.CategoryRoomData
import com.method.highpoint.model.roomdb.country.CountryRoomData
import com.method.highpoint.model.roomdb.dates.DatesRoomData
import com.method.highpoint.model.roomdb.exhibitor.ExhibitorRoomData
import com.method.highpoint.model.roomdb.hours.HoursRoomData
import com.method.highpoint.model.roomdb.maps.MapRoomLocations
import com.method.highpoint.model.roomdb.options.OptionRoomData
import com.method.highpoint.model.roomdb.pricepoint.PricePointRoomData
import com.method.highpoint.model.roomdb.shuttle.ShuttleLinesRoom
import com.method.highpoint.model.roomdb.shuttle.ShuttleRoomData
import com.method.highpoint.model.roomdb.style.StyleRoomData

@Database(entities = [ExhibitorRoomData::class, EventRoomData::class, HoursRoomData::class, DatesRoomData::class, AreaRoomData::class, BuildingRoomData::class, CategoryRoomData::class,
                     CountryRoomData::class, OptionRoomData::class, PricePointRoomData::class, StyleRoomData::class, ShuttleRoomData::class, ShuttleLinesRoom::class, MapRoomLocations::class],
    version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HighPointRoomDatabase : RoomDatabase() {

    abstract fun exhibitorRoomDataDao() : ExhibitorRoomDataDao

    companion object {

        @Volatile
        private var INSTANCE : HighPointRoomDatabase?= null

        fun getDatabase(context: Context, scope: CoroutineScope) : HighPointRoomDatabase {
            //if instance is not null then return it...
            //if it is null then create DB
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HighPointRoomDatabase::class.java,
                    "highpoint_database"
                )
                    .addCallback(HighPointDatabaseCallBack(scope))
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private class HighPointDatabaseCallBack(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }

        suspend fun populateDatabase(exhibitorRoomDataDao: ExhibitorRoomDataDao) {
            exhibitorRoomDataDao.deleteAll()
        }
    }
}