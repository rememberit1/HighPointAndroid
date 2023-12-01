package com.method.highpoint.model.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.method.highpoint.model.roomdb.area.AreaRoomData
import com.method.highpoint.model.roomdb.building.BuildingRoomData
import com.method.highpoint.model.roomdb.category.CategoryRoomData
import com.method.highpoint.model.roomdb.country.CountryRoomData
import com.method.highpoint.model.roomdb.dates.DatesRoomData
import com.method.highpoint.model.roomdb.event.EventRoomData
import com.method.highpoint.model.roomdb.exhibitor.ExhibitorRoomData
import com.method.highpoint.model.roomdb.hours.HoursRoomData
import com.method.highpoint.model.roomdb.maps.MapRoomLocations
import com.method.highpoint.model.roomdb.options.OptionRoomData
import com.method.highpoint.model.roomdb.pricepoint.PricePointRoomData
import com.method.highpoint.model.roomdb.shuttle.ShuttleLinesRoom
import com.method.highpoint.model.roomdb.shuttle.ShuttleRoomData
import com.method.highpoint.model.roomdb.style.StyleRoomData

@Dao
interface ExhibitorRoomDataDao {

    @Query("SELECT * FROM exhibitor_data ORDER BY exhibitorName ASC")
    fun getExhibitorSortedData() : LiveData<List<ExhibitorRoomData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exhibitorData: ExhibitorRoomData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArea(areaData: AreaRoomData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBuilding(buildingRoomData: BuildingRoomData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCountry(countryRoomData: CountryRoomData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(categoryRoomData: CategoryRoomData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOption(optionRoomData: OptionRoomData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPricePoint(pricePointRoomData: PricePointRoomData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStyle(styleRoomData: StyleRoomData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShuttleStops(shuttleRoomData: ShuttleRoomData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShuttleLines(shuttleLinesRoom: ShuttleLinesRoom)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMapLocations(mapRoomLocations: MapRoomLocations)

    @Query("DELETE FROM exhibitor_data")
    suspend fun deleteAll()

    @Query("SELECT * FROM exhibitor_data WHERE exhibitorName LIKE :query " +
            "OR exhibitorShowroom LIKE :query " +
            "OR buildingAddress LIKE :query")
    fun searchFor(query: String) : LiveData<List<ExhibitorRoomData>>

    @Query("SELECT buildingAddress FROM exhibitor_data WHERE buildingId LIKE :query")
    fun searchForBuildingAddress(query: String) : String

    @Query("SELECT exhibitorShowroom FROM exhibitor_data WHERE exhibitorId LIKE :query")
    fun searchForExhibitorAddress(query: String) : String

    @Query("SELECT * FROM exhibitor_data WHERE exhibitorCountry LIKE :filterCountry " +
            "AND buildingName LIKE :filterBuilding " +
            "AND exhibitorAreaInterest LIKE :filterArea ")
    fun filterFor(filterCountry: String, filterBuilding: String, filterArea: String) : LiveData<List<ExhibitorRoomData>>

    @Query("SELECT * FROM exhibitor_data WHERE exhibitorId = :id")
    fun getMyMarket(id: Int): LiveData<List<ExhibitorRoomData>>

    @Query("SELECT * FROM event_data WHERE eventId = :id")
    fun getMyEvent(id: Int): LiveData<List<EventRoomData>>

    @Query("SELECT * FROM area_data")
    fun getAreaData() : LiveData<List<AreaRoomData>>

    @Query("SELECT * FROM building_data")
    fun getBuildingData() : LiveData<List<BuildingRoomData>>

    @Query("SELECT * FROM country_data")
    fun getCountryData() : LiveData<List<CountryRoomData>>

    @Query("SELECT * FROM category_data")
    fun getCategoryData() : LiveData<List<CategoryRoomData>>

    @Query("SELECT * FROM option_data")
    fun getOptionData() : LiveData<List<OptionRoomData>>

    @Query("SELECT * FROM price_point_data")
    fun getPricePointData() : LiveData<List<PricePointRoomData>>

    @Query("SELECT * FROM style_data")
    fun getStyleData() : LiveData<List<StyleRoomData>>

    @Query("SELECT * FROM shuttle_stops")
    fun getShuttleStops() : LiveData<List<ShuttleRoomData>>

    @Query("SELECT * FROM shuttle_lines")
    fun getShuttleLines() : LiveData<List<ShuttleLinesRoom>>

    @Query("SELECT * FROM event_data")
    fun getEventData() : LiveData<List<EventRoomData>>

    @Query("SELECT * FROM map_locations")
    fun getMapLocationsData() : LiveData<List<MapRoomLocations>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEventData(eventRoomData: EventRoomData)

    @Query("SELECT * FROM date_data")
    fun getDateData() : LiveData<List<DatesRoomData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDatesData(datesRoomData: DatesRoomData)

    @Query("SELECT * FROM hours_data")
    fun getHoursData() : LiveData<List<HoursRoomData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHoursData(hoursRoomData: HoursRoomData)

    @Query("SELECT * FROM event_data WHERE eventTitle LIKE :query " +
            "OR eventLocation LIKE :query ")
    fun searchForEvents(query: String) : LiveData<List<EventRoomData>>

    @Query("SELECT * FROM event_data WHERE eventType LIKE :filter ")
    fun eventFilterFor(filter: String): LiveData<List<EventRoomData>>
}