package com.method.highpoint


import android.os.Build
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.method.highpoint.databinding.ActivityMainBinding
import android.util.Log
import androidx.annotation.RequiresApi
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.method.highpoint.views.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.method.highpoint.model.roomdb.event.EventRoomData
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
import com.method.highpoint.utils.isOnline
import com.method.highpoint.views.signin.LoginFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private val defaultGuid = ""
    lateinit var userGuid: String
    val TAG = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        val sharedPreference = getPreferences(Context.MODE_PRIVATE) ?: return
        userGuid = sharedPreference.getString("UserGUID", defaultGuid).toString()
        if (userGuid != defaultGuid) {
            replaceFragment(MyMarketFragment("default"))
        } else {
            replaceFragment(LoginFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.my_market -> {
                    userGuid = sharedPreference.getString("UserGUID", defaultGuid).toString()
                    if (userGuid != defaultGuid) {
                        replaceFragment(MyMarketFragment("default"))
                    } else {
                        replaceFragment(LoginFragment())
                    }
                }
                R.id.exhibitors -> {
                    replaceFragment(ExhibitorsFragment())
                }
                R.id.events -> {
                    replaceFragment(EventsFragment())
                }
                R.id.map -> {
                    replaceFragment(MapFragment("All", "All"))
                }
                else -> {
                    replaceFragment(AboutFragment())
                }
            }
            true
        }

        //check if network connection is active
        if (isOnline(applicationContext)) {
            callData()
        }

        binding.bottomNavigation.setItemIconTintList(null)
    }

    private fun callData() {
        viewModel.fetchExhibitorArea()
        viewModel.fetchBuildingName()
        viewModel.fetchCategories()
        viewModel.fetchCountries()
        viewModel.fetchDates()
        viewModel.fetchEvents()
        viewModel.fetchExhibitorDetails()
        viewModel.fetchExhibitorFilters()
        viewModel.fetchExhibitorUpdates()
        viewModel.fetchOptions()
        viewModel.fetchPricePoints()
        viewModel.fetchStyles()
        viewModel.fetchShuttleDetails()
        viewModel.fetchShuttleLocationDetails()
        viewModel.fetchMapLocations()

        //Insert Exhibitor Data into Room DB
        viewModel.exhibitorFilterModelLiveData?.observe(this, Observer { exhibitorData ->
            exhibitorData?.forEach { exhibitors ->
                var number = 1
                val exhibitorRoomData = ExhibitorRoomData(
                    number, exhibitorName = exhibitors.exhibitorName!!, buildingAddress = exhibitors.buildingAddress,
                    buildingFloor = exhibitors.buildingFloor, buildingFloorSort = exhibitors.buildingFloorSort,
                    buildingId = exhibitors.buildingId, buildingMultiTenant = exhibitors.buildingMultiTenant,
                    buildingName = exhibitors.buildingName, buildingResourceCode = exhibitors.buildingResourceCode,
                    buildingWing = exhibitors.buildingWing, exhibitorAreaInterest = exhibitors.exhibitorAreaInterest,
                    exhibitorBustop = exhibitors.exhibitorBustop, exhibitorBustopType = exhibitors.exhibitorBustopType,
                    exhibitorNeighborhood = exhibitors.exhibitorNeighborhood, exhibitorCountry = exhibitors.exhibitorCountry,
                    exhibitorDescription = exhibitors.exhibitorDescription, exhibitorFacebook = exhibitors.exhibitorFacebook,
                    exhibitorId = exhibitors.exhibitorId, exhibitorInstagram = exhibitors.exhibitorInstagram,
                    exhibitorLinkedin = exhibitors.exhibitorLinkedin, exhibitorPinterest = exhibitors.exhibitorPinterest,
                    exhibitorShowroom = exhibitors.exhibitorShowroom, exhibitorShowroomPhone = exhibitors.exhibitorShowroomPhone,
                    exhibitorStatus = exhibitors.exhibitorStatus, exhibitorTwitter = exhibitors.exhibitorTwitter,
                    exhibitorWebsite = exhibitors.exhibitorWebsite, exhibitorYoutube = exhibitors.exhibitorYoutube,
                    filter = exhibitors.filters
                )
                viewModel.insertExhibitorRoomData(exhibitorRoomData)
                number++
            }
        })


        viewModel.eventModelLiveData?.observe(this, Observer { eventData ->
            eventData?.forEach { event ->
                var number = 1
                val eventRoomData = EventRoomData(
                    number, eventTitle = event.eventTitle!!, eventBuilding = event.eventBuilding,
                    eventSummary = event.eventSummary, eventId = event.eventId,
                    eventLocation = event.eventLocation, eventStartDateTime = event.eventStartDateTime,
                    eventEndDateTime = event.eventEndDateTime, eventType = event.eventType
                )
                viewModel.insertEventRoomData(eventRoomData)
                number++
            }
        })

        viewModel.dateModelLiveData?.observe(this, Observer { dateData ->
            dateData?.marketDates?.forEach { date ->
                var number = 1
                val datesRoomData = DatesRoomData(number, id = date.id, active = date.active, closingDate = date.closingDate,
                    openingDate =  date.openingDate, marketName =  date.marketName, eventDate = date.eventDate)
                viewModel.insertDateRoomData(datesRoomData)
                number++
            }
        })

        viewModel.areaModelLiveData?.observe(this, Observer { areaData ->
            areaData?.forEach { area ->
                if (area.exhibitorAreaInterest != null) {
                    val areaRoomData = AreaRoomData(
                        exhibitorAreaInterest = area.exhibitorAreaInterest
                    )
                    viewModel.insertAreaRoomData(areaRoomData)
                }
            }
        })

        viewModel.buildingModelLiveData?.observe(this, Observer { buildingData ->
            buildingData?.forEach { building ->
                val buildingRoomData = BuildingRoomData(
                    buildingId = building.buildingId, buildingName = building.buildingName
                )
                viewModel.insertBuildingRoomData(buildingRoomData)
            }
        })

        viewModel.categoryModelLiveData?.observe(this, Observer { categoryData ->
            categoryData?.forEach { category ->
                val categoryRoomData = CategoryRoomData(
                    categoryId = category.categoryId, parentId = category.parentId,
                    categoryParentName = category.categoryParentName, categorySubName = category.categorySubName
                )
                viewModel.insertCategoryRoomData(categoryRoomData)
            }
        })

        viewModel.countryModelLiveData?.observe(this, Observer { countryData ->
            countryData?.forEach { country ->
                val countryRoomData = CountryRoomData(
                    exhibitorCountry = country.exhibitorCountry!!
                )
                viewModel.insertCountryRoomData(countryRoomData)
            }
        })

        viewModel.optionModelLiveData?.observe(this, Observer { optionData ->
            optionData?.forEach { option ->
                val optionRoomData = OptionRoomData(
                    optionId = option.optionId!!, optionName = option.optionName
                )
                viewModel.insertOptionRoomData(optionRoomData)
            }
        })

        viewModel.pricePointsModelLiveData?.observe(this, Observer { pricePointData ->
            pricePointData?.forEach { pricePoint ->
                val pricePointRoomData = PricePointRoomData(
                    pricePointId = pricePoint.pricePointId,
                    pricePointName = pricePoint.pricePointName
                )
                viewModel.insertPricePointRoomData(pricePointRoomData)
            }
        })

        viewModel.styleModelLiveData?.observe(this, Observer { styleData ->
            styleData?.forEach { style ->
                val styleRoomData = StyleRoomData(
                    styleId = style.styleId!!,
                    styleName = style.styleName
                )
                viewModel.insertStyleRoomData(styleRoomData)
            }
        })

        viewModel.shuttlesModelLiveData?.observe(this) { shuttleModelData ->
            shuttleModelData?.forEach { shuttleData ->
                val shuttleRoomData = ShuttleRoomData(
                    stopId = shuttleData.stopId,
                    stopNumber = shuttleData.stopNumber,
                    stopDescription = shuttleData.stopDescription,
                    longitude = shuttleData.longitude,
                    latitude = shuttleData.latitude,
                    line = shuttleData.line
                )
                viewModel.insertShuttleStopsData(shuttleRoomData)
            }
        }

        viewModel.shuttlesLineLiveData?.observe(this) { shuttleLinesData ->
            var row = 1
            shuttleLinesData?.forEach { shuttlesLine ->
                val shuttleLinesRoom = ShuttleLinesRoom(
                    rowId = row,
                    shuttleLine = shuttlesLine.shuttleLine,
                    stopNumberStart = shuttlesLine.stopNumberStart,
                    stopNumberEnd = shuttlesLine.stopNumberEnd,
                    shuttleLineLocations = shuttlesLine.shuttleLineLocations
                )
                viewModel.insertShuttleLinesData(shuttleLinesRoom)
                row++
            }
        }

        viewModel.mapLocationsLiveData?.observe(this) { mapLocationsData ->
            var row = 1
            mapLocationsData?.forEach { mapLocations ->
                val mapRoomLocations = MapRoomLocations(
                    rowId = row,
                    buildingId = mapLocations.buildingId,
                    buildingName = mapLocations.buildingName,
                    exhibitorId = mapLocations.exhibitorId,
                    latitude = mapLocations.latitude,
                    longitude = mapLocations.longitude,
                    tenantCount = mapLocations.tenantCount,
                    buildingExhibitors = mapLocations.buildingExhibitors
                )
                viewModel.insertMapLocationsData(mapRoomLocations)
                row++
            }
        }
    }



    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
