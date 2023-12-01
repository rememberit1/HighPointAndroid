package com.method.highpoint.model.roomdb

import androidx.lifecycle.LiveData
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

class ExhibitorRoomRepository(private val exhibitorRoomDataDao: ExhibitorRoomDataDao) {


    suspend fun insert(exhibitorRoomData: ExhibitorRoomData) {
        exhibitorRoomDataDao.insert(exhibitorRoomData)
    }

    suspend fun insertEventData(eventRoomData: EventRoomData) {
        exhibitorRoomDataDao.insertEventData(eventRoomData)
    }

    suspend fun insertDateData(datesRoomData: DatesRoomData) {
        exhibitorRoomDataDao.insertDatesData(datesRoomData)
    }

    suspend fun insertHoursData(hoursRoomData: HoursRoomData) {
        exhibitorRoomDataDao.insertHoursData(hoursRoomData)
    }


    fun getEventData(): LiveData<List<EventRoomData>> {
        return exhibitorRoomDataDao.getEventData()
    }

    fun getDatesData(): LiveData<List<DatesRoomData>> {
        return exhibitorRoomDataDao.getDateData()
    }

    fun getHoursData(): LiveData<List<HoursRoomData>> {
        return exhibitorRoomDataDao.getHoursData()
    }


    fun getSearchResultsEvents(query: String): LiveData<List<EventRoomData>> {
        return exhibitorRoomDataDao.searchForEvents(query)
    }

    fun getEventFilterResults(filter: Boolean): LiveData<List<EventRoomData>> {
        val filterString: String = if (filter) {
            "Social"
        } else {
            "Educational"
        }
        return exhibitorRoomDataDao.eventFilterFor(filterString)
    }

    suspend fun insertArea(areaRoomData: AreaRoomData) {
        exhibitorRoomDataDao.insertArea(areaRoomData)
    }

    suspend fun insertBuilding(buildingRoomData: BuildingRoomData) {
        exhibitorRoomDataDao.insertBuilding(buildingRoomData)
    }

    suspend fun insertCategory(categoryRoomData: CategoryRoomData) {
        exhibitorRoomDataDao.insertCategory(categoryRoomData)
    }

    suspend fun insertCountry(countryRoomData: CountryRoomData) {
        exhibitorRoomDataDao.insertCountry(countryRoomData)
    }

    suspend fun insertOption(optionRoomData: OptionRoomData) {
        exhibitorRoomDataDao.insertOption(optionRoomData)
    }

    suspend fun insertPricePoint(pricePointRoomData: PricePointRoomData) {
        exhibitorRoomDataDao.insertPricePoint(pricePointRoomData)
    }

    suspend fun insertStyle(styleRoomData: StyleRoomData) {
        exhibitorRoomDataDao.insertStyle(styleRoomData)
    }

    suspend fun insertShuttleStops(shuttleRoomData: ShuttleRoomData) {
        exhibitorRoomDataDao.insertShuttleStops(shuttleRoomData)
    }

    suspend fun insertShuttleLines(shuttleLinesRoom: ShuttleLinesRoom) {
        exhibitorRoomDataDao.insertShuttleLines(shuttleLinesRoom)
    }

    suspend fun insertMapLocations(mapRoomLocations: MapRoomLocations) {
        exhibitorRoomDataDao.insertMapLocations(mapRoomLocations)
    }

    fun getSearchResults(query: String): LiveData<List<ExhibitorRoomData>> {
        return exhibitorRoomDataDao.searchFor(query)
    }

    fun getBuildingAddress(query: String): String {
        return exhibitorRoomDataDao.searchForBuildingAddress(query)
    }

    fun getExhibitorAddress(query: String): String {
        return exhibitorRoomDataDao.searchForExhibitorAddress(query)
    }

    fun getFilterResults(filterCountry: String, filterBuilding: String, filterArea: String): LiveData<List<ExhibitorRoomData>> {
        return exhibitorRoomDataDao.filterFor(filterCountry, filterBuilding, filterArea)
    }

    fun getMyMarket(id: Int): LiveData<List<ExhibitorRoomData>> {
        return exhibitorRoomDataDao.getMyMarket(id)
    }

    fun getMyEvent(id: Int): LiveData<List<EventRoomData>> {
        return exhibitorRoomDataDao.getMyEvent(id)
    }

    fun getAreaData(): LiveData<List<AreaRoomData>> {
        return exhibitorRoomDataDao.getAreaData()
    }

    fun getBuildingData(): LiveData<List<BuildingRoomData>> {
        return exhibitorRoomDataDao.getBuildingData()
    }

    fun getCategoryData(): LiveData<List<CategoryRoomData>> {
        return exhibitorRoomDataDao.getCategoryData()
    }

    fun getCountryData(): LiveData<List<CountryRoomData>> {
        return exhibitorRoomDataDao.getCountryData()
    }

    fun getOptionData(): LiveData<List<OptionRoomData>> {
        return exhibitorRoomDataDao.getOptionData()
    }

    fun getPricePointData(): LiveData<List<PricePointRoomData>> {
        return exhibitorRoomDataDao.getPricePointData()
    }

    fun getStyleData(): LiveData<List<StyleRoomData>> {
        return exhibitorRoomDataDao.getStyleData()
    }

    fun getShuttleStopsData(): LiveData<List<ShuttleRoomData>> {
        return exhibitorRoomDataDao.getShuttleStops()
    }

    fun getShuttleLinesData(): LiveData<List<ShuttleLinesRoom>> {
        return exhibitorRoomDataDao.getShuttleLines()
    }

    fun getMapLocationsData(): LiveData<List<MapRoomLocations>> {
        return exhibitorRoomDataDao.getMapLocationsData()
    }
}