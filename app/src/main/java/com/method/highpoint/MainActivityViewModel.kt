package com.method.highpoint

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.method.highpoint.model.area.AreaModel
import com.method.highpoint.model.building.BuildingModel
import com.method.highpoint.model.category.CategoryModel
import com.method.highpoint.model.country.CountryModel
import com.method.highpoint.model.dates.DatesModel
import com.method.highpoint.model.event.EventModel
import com.method.highpoint.model.exhibitor.ExhibitorFilterModel
import com.method.highpoint.model.exhibitor.ExhibitorModel
import com.method.highpoint.model.exhibitor.ExhibitorUpdateModel
import com.method.highpoint.model.mymarket.MyMarketItem
import com.method.highpoint.model.maps.MapLocations
import com.method.highpoint.model.mymarket.MyMarketResponse
import com.method.highpoint.model.mymarket.MyMarketStar
import com.method.highpoint.model.options.OptionModel
import com.method.highpoint.model.pricepoint.PricePointsModel
import com.method.highpoint.model.roomdb.area.AreaRoomData
import com.method.highpoint.model.roomdb.building.BuildingRoomData
import com.method.highpoint.model.roomdb.category.CategoryRoomData
import com.method.highpoint.model.roomdb.country.CountryRoomData
import com.method.highpoint.model.roomdb.exhibitor.ExhibitorRoomData
import com.method.highpoint.model.roomdb.ExhibitorRoomRepository

import com.method.highpoint.model.roomdb.HighPointRoomDatabase
import com.method.highpoint.model.roomdb.dates.DatesRoomData

import com.method.highpoint.model.roomdb.event.EventRoomData
import com.method.highpoint.model.roomdb.hours.HoursRoomData
import com.method.highpoint.model.roomdb.maps.MapRoomLocations
import com.method.highpoint.model.roomdb.options.OptionRoomData
import com.method.highpoint.model.roomdb.pricepoint.PricePointRoomData
import com.method.highpoint.model.roomdb.shuttle.ShuttleLinesRoom
import com.method.highpoint.model.roomdb.shuttle.ShuttleRoomData
import com.method.highpoint.model.roomdb.style.StyleRoomData
import com.method.highpoint.model.shuttle.ShuttlesLine
import com.method.highpoint.model.style.StylesModel
import com.method.highpoint.model.shuttle.ShuttlesModel
import com.method.highpoint.model.signin.ForgotPassword
import com.method.highpoint.model.signin.UserInfo
import com.method.highpoint.repository.ApiRepository
import com.method.highpoint.repository.JanRainApiRepository
import kotlinx.coroutines.launch


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var apiRepository: ApiRepository? = null
    private var janRainApiRepository: JanRainApiRepository? = null
    var areaModelLiveData : LiveData<List<AreaModel>?>? = null
    var buildingModelLiveData : LiveData<List<BuildingModel>?>? = null
    var categoryModelLiveData : LiveData<List<CategoryModel>?>? = null
    var countryModelLiveData : LiveData<List<CountryModel>?>? = null
    var dateModelLiveData : LiveData<DatesModel?>? = null
    var eventModelLiveData : LiveData<List<EventModel>?>? = null
    var exhibitorModelLiveData : LiveData<List<ExhibitorModel>?>? = null
    var exhibitorFilterModelLiveData : LiveData<List<ExhibitorFilterModel>?>? = null
    var exhibitorUpdateModelLiveData : LiveData<List<ExhibitorUpdateModel>?>? = null
    var styleModelLiveData : LiveData<List<StylesModel>?>? = null
    var pricePointsModelLiveData : LiveData<List<PricePointsModel>?>? = null
    var optionModelLiveData : LiveData<List<OptionModel>?>? = null
    var myMarketResponseLiveData : LiveData<MyMarketResponse?>? = null
    var myMarketItemLiveData : LiveData<MyMarketItem?>? = null
    var datesRoomLiveData : LiveData<List<DatesRoomData>>? = null
    var hoursRoomLiveData : LiveData<List<HoursRoomData>>? = null

    var shuttlesModelLiveData : LiveData<List<ShuttlesModel>?>? = null
    var shuttlesLineLiveData : LiveData<List<ShuttlesLine>?>? = null
    var mapLocationsLiveData : LiveData<List<MapLocations>?>? = null
    var myMapLocationsLiveData : LiveData<List<MapLocations>?>? = null

    private val exhibitorRoomRepository : ExhibitorRoomRepository
    var exhibitorRoomData : LiveData<List<ExhibitorRoomData>>
    var eventRoomData : LiveData<List<EventRoomData>>

    val query = MutableLiveData<String>()
    val queryEvents = MutableLiveData<String>()
    var userInfo: UserInfo? = null
    var forgotPassword: ForgotPassword? = null

    init {
        apiRepository = ApiRepository(application.applicationContext)
        janRainApiRepository = JanRainApiRepository()
        areaModelLiveData = MutableLiveData()
        buildingModelLiveData = MutableLiveData()
        categoryModelLiveData = MutableLiveData()
        countryModelLiveData = MutableLiveData()
        dateModelLiveData = MutableLiveData()
        eventModelLiveData = MutableLiveData()
        exhibitorModelLiveData = MutableLiveData()
        exhibitorFilterModelLiveData = MutableLiveData()
        exhibitorUpdateModelLiveData = MutableLiveData()
        styleModelLiveData = MutableLiveData()
        pricePointsModelLiveData = MutableLiveData()
        myMarketResponseLiveData = MutableLiveData()
        shuttlesModelLiveData = MutableLiveData()
        mapLocationsLiveData = MutableLiveData()
        myMapLocationsLiveData = MutableLiveData()
        myMarketItemLiveData = MutableLiveData()
        datesRoomLiveData = MutableLiveData()

        //Room DB
        val exhibitorRoomDataDao = HighPointRoomDatabase.getDatabase(application, viewModelScope).exhibitorRoomDataDao()
        exhibitorRoomRepository = ExhibitorRoomRepository(exhibitorRoomDataDao)
        exhibitorRoomData = Transformations.switchMap(query, ::getExhibitorRoomData)
        query.value = "%%"

        eventRoomData = Transformations.switchMap(queryEvents, ::getEventDataForQuery)
        queryEvents.value = "%%"

    }

    fun fetchExhibitorArea() {
        viewModelScope.launch {
            areaModelLiveData = apiRepository?.fetchExhibitorArea()
        }
    }

    fun fetchStyles() {
        viewModelScope.launch {
            styleModelLiveData = apiRepository?.fetchStyles()
        }
    }

    fun fetchOptions() {
        viewModelScope.launch {
            optionModelLiveData = apiRepository?.fetchOptions()
        }
    }

    fun fetchPricePoints() {
        viewModelScope.launch {
            pricePointsModelLiveData = apiRepository?.fetchPricePoints()
        }
    }

    fun fetchBuildingName() {
        buildingModelLiveData = apiRepository?.fetchBuildingName()
    }

    fun fetchCategories() {
        categoryModelLiveData = apiRepository?.fetchCategories()
    }

    fun fetchCountries() {
        countryModelLiveData = apiRepository?.fetchCountry()
    }

    fun fetchDates() {
        dateModelLiveData = apiRepository?.fetchDates()
    }

    fun fetchEvents() {
        eventModelLiveData = apiRepository?.fetchEvents()
    }

    fun fetchExhibitorDetails() {
        exhibitorModelLiveData = apiRepository?.fetchExhibitorDetails()
    }

    fun fetchExhibitorFilters() {
        exhibitorFilterModelLiveData = apiRepository?.fetchExhibitorFilters()
    }

    fun fetchExhibitorUpdates() {
        exhibitorUpdateModelLiveData = apiRepository?.fetchExhibitorUpdates()
    }

    fun fetchMyMarketDetails(userGuid: String) {
        myMarketResponseLiveData = apiRepository?.fetchMyMarket(userGuid)
    }

    fun fetchShuttleDetails() {
        shuttlesModelLiveData = apiRepository?.fetchShuttleDetails()
    }

    fun fetchShuttleLocationDetails() {
        shuttlesLineLiveData = apiRepository?.fetchShuttleLineDetails()
    }

    fun fetchMapLocations() {
        mapLocationsLiveData = apiRepository?.fetchMapLocations()
    }

    fun fetchMyMapLocations(userGuid: String) {
        myMapLocationsLiveData = apiRepository?.fetchMyMapLocations(userGuid)
    }

    //Search for Exhibitor Data in Room DB
    fun getExhibitorRoomData(query: String) = exhibitorRoomRepository.getSearchResults("%$query%")

    fun getBuildingAddress(query: String) = exhibitorRoomRepository.getBuildingAddress("%$query%")

    fun getExhibitorAddress(query: String) = exhibitorRoomRepository.getExhibitorAddress("%$query%")



      fun getEventDataInView() = exhibitorRoomRepository.getEventData()
      fun getDatesRoomData () {
          datesRoomLiveData=  exhibitorRoomRepository.getDatesData()
      }
    fun getHoursRoomData () {
        hoursRoomLiveData=  exhibitorRoomRepository.getHoursData()
    }
      fun getEventDataForQuery(query: String) = exhibitorRoomRepository.getSearchResultsEvents("%$query%")

    //Insert Exhibitor Data into Room DB

    fun getFilterExhibitorRoomData(filterCountry: String, filterBuilding: String, filterArea: String) =
        exhibitorRoomRepository.getFilterResults("%$filterCountry%", "%$filterBuilding%", "%$filterArea%")

    fun getMyMarket(id: Int) = exhibitorRoomRepository.getMyMarket(id)

    fun getMyEvents(id: Int) = exhibitorRoomRepository.getMyEvent(id)

    //Insert Data into Room DB

    fun insertExhibitorRoomData(exhibitorRoomData: ExhibitorRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insert(exhibitorRoomData)
    }


    //Insert Event Data into Room DB
    fun insertEventRoomData(eventRoomData: EventRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insertEventData(eventRoomData)
    }

    fun insertDateRoomData(datesRoomData: DatesRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insertDateData(datesRoomData)
    }

    fun insertHoursRoomData(hoursRoomData: HoursRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insertHoursData(hoursRoomData)
    }

    fun getFilterEventRoomData(SocialPickedSetTrueOrEducationPickedSetFalse: Boolean) =
            exhibitorRoomRepository.getEventFilterResults(SocialPickedSetTrueOrEducationPickedSetFalse)

    fun insertAreaRoomData(areaRoomData: AreaRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insertArea(areaRoomData)
    }

    fun insertBuildingRoomData(buildingRoomData: BuildingRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insertBuilding(buildingRoomData)
    }

    fun insertCategoryRoomData(categoryRoomData: CategoryRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insertCategory(categoryRoomData)
    }

    fun insertCountryRoomData(countryRoomData: CountryRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insertCountry(countryRoomData)
    }

    fun insertOptionRoomData(optionRoomData: OptionRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insertOption(optionRoomData)
    }

    fun insertPricePointRoomData(pricePointRoomData: PricePointRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insertPricePoint(pricePointRoomData)
    }

    fun insertStyleRoomData(styleRoomData: StyleRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insertStyle(styleRoomData)
    }

    fun insertShuttleStopsData(shuttleRoomData: ShuttleRoomData) = viewModelScope.launch {
        exhibitorRoomRepository.insertShuttleStops(shuttleRoomData)
    }

    fun insertShuttleLinesData(shuttleLinesRoom: ShuttleLinesRoom) = viewModelScope.launch {
        exhibitorRoomRepository.insertShuttleLines(shuttleLinesRoom)
    }

    fun insertMapLocationsData(mapRoomLocations: MapRoomLocations) = viewModelScope.launch {
        exhibitorRoomRepository.insertMapLocations(mapRoomLocations)
    }

    //get data from Room DB
    fun getAreaData() = exhibitorRoomRepository.getAreaData()

    fun getBuildingData() = exhibitorRoomRepository.getBuildingData()

    fun getCategoryData() = exhibitorRoomRepository.getCategoryData()

    fun getCountryData() = exhibitorRoomRepository.getCountryData()

    fun getOptionData() = exhibitorRoomRepository.getOptionData()

    fun getPricePointData() = exhibitorRoomRepository.getPricePointData()

    fun getStyleData() = exhibitorRoomRepository.getStyleData()

    fun getShuttleData() = exhibitorRoomRepository.getShuttleStopsData()

    fun getShuttleLines() = exhibitorRoomRepository.getShuttleLinesData()

    fun getMapLocationsData() = exhibitorRoomRepository.getMapLocationsData()

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            userInfo = janRainApiRepository?.signInUser(username, password)
        }
    }

    fun registerUser(emailAddress: String, newPassword: String, lastName: String,
                     firstName: String, displayName: String = "$lastName $firstName") {
        viewModelScope.launch {
            userInfo = janRainApiRepository?.registerUser(emailAddress, newPassword, lastName, firstName, displayName)
        }
    }

    fun forgotPassword(emailAddress: String) {
        viewModelScope.launch {
            forgotPassword = janRainApiRepository?.forgotPassword(emailAddress)
        }
    }

    fun isLoading(): LiveData<Boolean> {
        return apiRepository!!.isLoading
    }

}